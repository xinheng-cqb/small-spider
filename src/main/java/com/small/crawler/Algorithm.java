package com.small.crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caiqibin
 * @date 2017年9月5日
 * @introduce: 排序算法
 */
public class Algorithm {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		list.add(234);
		list.add(24);
		list.add(11);
		list.add(55);
		list.add(3);
		list.add(82);
		list.add(7);
		radixSort(list, 0, list.size() - 1, 3);
		System.out.println(list);
	}

	public static int getDigit(int x, int d) {
		int[] a = { 1, 1, 10, 100 };
		return (x / a[d]) % 10;
	}

	public static void radixSort(List<Integer> list, int begin, int end, int digit) {
		final int radix = 10;
		int[] count = new int[radix];
		int[] bucket = new int[end - begin + 1];

		for (int d = 1; d <= digit; d++) {

			for (int i = 0; i < radix; i++) {
				count[i] = 0;
			}

			for (int i = begin; i <= end; i++) {
				int j = getDigit(list.get(i), d);
				count[j]++;
			}

			for (int i = 1; i < radix; i++) {
				count[i] += count[i - 1];
			}

			for (int i = end; i >= begin; i--) {
				int j = getDigit(list.get(i), d);
				bucket[count[j] - 1] = list.get(i);
				count[j]--;
			}

			for (int i = begin, j = 0; i <= end; i++, j++) {
				list.set(i, bucket[j]);
			}
		}
	}

}
