package top.xcore.xdata;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlzhao on 2017/2/21.
 */
public class XPath {
    XData data;
    List<Integer> calls = new ArrayList<>();
    public XPath(XData data) {
        this.data = data;
    }
    public XPath field(int index) {
        calls.add(index);
        return this;
    }

    public int callInt() {
        int i = 0;
        XData work = data;
        while(i < calls.size()) {
            int index = calls.get(i);
            if (index > XType.TYPE_CUSTOMER_START) {
                XData next = work.getData(index);
                if (next == null) {
                    return 0;
                } else {
                    work = next;
                    i++;
                }
            } else {
                return data.getInteger(index);
            }
        }
        return 0;

    }

    public String callString() {
        int i = 0;
        XData work = data;
        while(i < calls.size()) {
            int index = calls.get(i);
            if (index > XType.TYPE_CUSTOMER_START) {
                XData next = work.getData(index);
                if (next == null) {
                    System.err.println("ACCESS EMPTY:" + TypeManager.getInstance().getFieldName(work.getType() ,index));
                    return "-EMPTY";
                } else {
                    work = next;
                    i++;
                }
            } else {
                return work.getString(index);
            }
        }
        return "";
    }

    public XData callData() {
        int i = 0;
        XData work = data;
        XData result = null;
        while(i < calls.size()) {
            int index = calls.get(i);
            if (index > XType.TYPE_CUSTOMER_START) {
                result = work.getData(index);
                if (result == null) {
                    return null;
                } else {
                    work = result;
                    i++;
                }
            } else {
                return null;
            }
        }
        return result;

    }
}
