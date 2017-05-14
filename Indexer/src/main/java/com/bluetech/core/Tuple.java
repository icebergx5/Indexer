/*
 * Copyright 2017 yasin.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bluetech.core;

/**
 *
 * @author yasin
 */
public class Tuple<T1, T2> {

    private T1 one;
    private T2 two;

    public Tuple(T1 one, T2 two) {
        this.one = one;
        this.two = two;
    }

    public T1 getOne() {
        return one;
    }

    public void setOne(T1 one) {
        this.one = one;
    }

    public T2 getTwo() {
        return two;
    }

    public void setTwo(T2 two) {
        this.two = two;
    }
}
