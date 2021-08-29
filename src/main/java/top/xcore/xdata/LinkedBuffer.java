package top.xcore.xdata;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wlzhao on 2017/7/18.
 */
public class LinkedBuffer {
    private Buffer head;
    private Buffer tail;
    private Buffer current;
    private int size;
    private int bufferCount;

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
        Buffer h = head;
        while(h != null) {
            outputStream.write(h.buffer,0,h.position);
            h = h.next;
        }
    }

    public byte[] toBytes() {
        int totalSize = getTotalSize();
        byte[] bytes =new byte[totalSize];
        Buffer h = head;
        int start = 0;
        int srcPos = 0;
        int count = totalSize / size;
        int left = totalSize % size;
        for(int i=0;i<count;i++) {
            System.arraycopy(h.buffer,srcPos,bytes,start,h.position);
            start += h.position;
            h = h.next;
        }
        if (h != null && left > 0) {
            System.arraycopy(h.buffer,srcPos,bytes,start,left);
        }
        return bytes;
    }

    public int getTotalSize() {
        int totalSize = (bufferCount - 1) * size;
        totalSize += tail.position;
        return totalSize;
    }

    private void increaseBuffer() {
        bufferCount ++;
        current.next = new Buffer(size);
        current.next.index = current.index+1;
        current = current.next;
        tail = current;
    }

    public LinkedBuffer(int size) {
        this.size = size;
        head = current = new Buffer(size);
        tail = head;
        bufferCount ++;
    }

    protected void writeByte(byte b) {
        int space = current.space();
        if (space > 0) {
            current.writeByte(b);
        } else if (current.next == null) {
            increaseBuffer();
            current.writeByte(b);
        } else {
            current = current.next;
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
                if (current.next == null) {
                    increaseBuffer();
                } else {
                    current = current.next;
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
        current.position = size;
        for(int i=0;i<bufferNeed;i++) {
            if (current.next == null) {
                increaseBuffer();
            } else {
                current.position = size;
                current = current.next;
            }
        }
        current.position = left;
    }

    public void seek(int position) {
        int index = position / size;
        int left = position % size;
        Buffer b = head;
        for(int i=0;i<index;i++) {
            b.position = size;
            b = b.next;
            if (b != null) {
                current = b;
            }
        }
        if (b != null) {
            b.position = left;
            current = b;
        }
    }

    public int getPosition() {
        return current.index * size + current.position;
    }
}
