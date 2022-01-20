package cn.cst.questions;

import java.util.Arrays;

public class Question1 {
  public static int singleNumber(int[] nums) {
    int[] ints = Arrays.stream(nums).sorted().toArray();
    if (nums.length == 1) {
      return nums[0];
    }
    int ans = -1;
    for (int index = 0; index < ints.length; index++) {
      if (judgeIs(ints, index)) {
        ans = index;
        break;
      }
    }
    return ints[ans];
  }

  static boolean judgeIs(int[] array, int index) {
    if (array.length == 1) {
      return true;
    } else {
      if (array.length > 3 && index == array.length - 1) {
        return !(array[index] == array[index - 1] || array[index] == array[index - 2]);
      }
      if (array.length > 3 && index == 0) {
        return !(array[index] == array[index + 1] || array[index] == array[index + 2]);
      } else {
        return !(array[index] == array[index + 1] || array[index] == array[index - 1]);
      }
    }
  }

  public static void main(String[] args) {
    int[] num = {0, 1, 0, 1, 0, 1, 99};
    System.out.println(singleNumber(num));
  }
}
