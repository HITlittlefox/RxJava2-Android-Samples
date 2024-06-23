package com.rxjava2.android.samples.ui.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by amitshekhar on 06/02/17.
 * 1. Subject同时充当了Observer和Observable的角色，Subject是非线程安全的，要避免该问题，需要将 Subject转换为一个 SerializedSubject，上述RxBus类中把线程非安全的PublishSubject包装成线程安全的Subject。
 * 2. PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者。
 */

// 放弃RxBus，拥抱RxJava（一）：为什么避免使用EventBus/RxBus
@Deprecated
public class RxBus {

    public RxBus() {
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
