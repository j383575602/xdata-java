package top.xcore.xdata;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlzhao on 2017/7/18.
 */
public class ListBuffer {

    private int size;
    private int bufferCount = 0;
    private List<Buffer> buffers = new ArrayList<>();
    private int currentIndex;
    private Buffer current;

    private class Buffer {
        private byte[] buffer;
        private Buffer next;
        private int position;
        private int index;
        private Buffer(int size) {
            buffer = new byte[size];
        }
        private void writeByte(byte b) {
           buffer[position ++] = b;
        }

        private void writeBytes(byte[] bytes,int start,int count) {
            System.arraycopy(bytes,start,buffer,position,count);
            position += count;
        }

        private int space() {
            return buffer.length - position;
        }
    }

    public void writeToStream(OutputStream outputStream) throws IOException{
        for(Buffer buffer : buffers) {
            outputStream.write(buffer.buffer,0,buffer.position);
        }
    }

    public byte[] toBytes() {
        int totalSize = getTotalSize();
        byte[] bytes =new byte[totalSize];
        int start = 0;
        int srcPos = 0;
        for(Buffer buffer : buffers) {
            System.arraycopy(buffer.buffer,srcPos,bytes,start,buffer.position);
            start += buffer.position;
        }
        return bytes;
    }

    public int getTotalSize() {
        int totalSize = (buffers.size() - 1) * size;
        totalSize += buffers.get(buffers.size()-1).position;
        return totalSize;
    }

    private void increaseBuffer() {
        Buffer buffer = new Buffer(size);
        buffers.add(buffer);
        current = buffer;
        currentIndex = bufferCount;
        bufferCount ++;
    }

    public ListBuffer(int size) {
        this.size = size;
        increaseBuffer();
    }

    public void writeByte(byte b) {
        int space = current.space();
        if (space > 0) {
            current.writeByte(b);
        } else if (currentIndex == buffers.size()-1) {
            increaseBuffer();
            current.writeByte(b);
        } else {
            currentIndex++;
            current = buffers.get(currentIndex);
            current.position = 0;
            current.writeByte(b);
        }
    }

    public void writeBytes(byte[] bytes) {
        int wrote = 0;
        int remain;
        while((remain = bytes.length - wrote) > 0) {
            int space = current.space();
            int count = Math.min(space,remain);
            current.writeBytes(bytes,wrote,count);
            if (space < remain) {
                if (currentIndex == buffers.size()-1) {
                    increaseBuffer();
                } else {
                    currentIndex++;
                    current = buffers.get(currentIndex);
                    current.position = 0;
                }
            }
            wrote += count;
        };
    }

    public void jump(int count) {
        int currentLeft = current.space();
        if (count <= currentLeft) {
            current.position += count;
            return;
        }
        int bufferNeed = (count - currentLeft + size -1) / size;
        int left = (count - currentLeft) % size;
        for(int i=0;i<bufferNeed;i++) {
            if (currentIndex < buffers.size()-1) {
                currentIndex++;
                current = buffers.get(currentIndex);
            } else {
                increaseBuffer();
            }

        }
        current.position = left;
    }



    public void seek(int position) {
        int index = position / size;
        int left = position % size;
        currentIndex = index;
        current = buffers.get(currentIndex);
        current.position = left;
    }

    public int getPosition() {
        return currentIndex * size + current.position;
    }
}
