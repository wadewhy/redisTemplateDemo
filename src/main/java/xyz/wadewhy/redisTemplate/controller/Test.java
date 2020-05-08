/**   
 * Copyright © 2020 eSunny Info. Tech Ltd. All rights reserved.
 * 作者博客：wadewhy.xyz
 * @Package: xyz.wadewhy.redisTemplate.controller 
 * @author: wadewhy   
 * @date: 2020年5月8日 上午10:37:57 
 */
package xyz.wadewhy.redisTemplate.controller;

public class Test {
    public static void test(int... a) {
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }

    public static void main(String[] args) {
        Test.test(1, 2);
    }
}
