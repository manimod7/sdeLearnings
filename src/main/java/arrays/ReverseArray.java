package arrays;

import java.util.Collections;
import java.util.List;

public class ReverseArray {
    private List<Integer> list;
    ReverseArray(List<Integer> list) {
        this.list = list;
    }

    public List<Integer> getList() {
        return this.list;
    }
    public void setList(List<Integer> list){
        this.list = list;
    }

    public  List<Integer> reverseListUsingCollection(List<Integer> list) {
        Collections.reverse(list);
        return list;
    }
    public  List<Integer> reverseListUsingSwapping(List<Integer> list) {
        for(int i = 0; i < list.size() / 2; i++) {
            Integer temp = list.get(i);
            list.set(i, list.get(list.size() - i - 1));
            list.set(list.size() - i - 1, temp);
        }
        return list;
    }
}
