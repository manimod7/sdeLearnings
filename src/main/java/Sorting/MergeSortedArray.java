package Sorting;

//Leetcode 88

public class MergeSortedArray {

    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int i=0,j=0, l = m+n,k=0;
        int [] nums = new int[l];
        while(i<m && j<n) {
            if(nums1[i]<=nums2[j]) {
                nums[k++] = nums1[i++];
            }
            else {
                nums[k++] = nums2[j++];
            }
        }
        while(i<m) {
            nums[k++] = nums1[i++];
        }
        while(j<n) {
            nums[k++] = nums2[j++];
        }
        i=0;
        while(i<m+n) {
            nums1[i] = nums[i];
            i++;
        }
    }
}
