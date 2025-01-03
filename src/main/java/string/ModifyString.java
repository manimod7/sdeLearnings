package string;

public class ModifyString {
    public static String solve(String A) {
        int n = A.length();
        if(n==0||n==1)
            return A;
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<A.length();i++) {
            if(i==0) {
                sb.append(A.charAt(i));
            }
            else {
                char prev = sb.charAt(sb.length()-1);
                char current = A.charAt(i);
                if(current==prev){
                    sb.deleteCharAt(sb.length()-1);
                }
                else {
                    sb.append(A.charAt(i));
                }
            }

        }
        return sb.toString();
    }
    public static void main (String[] args) {
        String s = "abccbc";
        String ans = ModifyString.solve(s);
    }
}
