package com.rxjava2.android.samples.ui.operators;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.utils.AppConstant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 * CompletableObserver也是属于Observer的一种，它只有onComplete回调，而没有类似与onNext之间的参数回调。
 */
public class CompletableObserverExampleActivity extends AppCompatActivity {

    private static final String TAG = CompletableObserverExampleActivity.class.getSimpleName();
    Button btn;
    Button btnC;
    Button btnCT;
    Button btnT;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = findViewById(R.id.btn);

        btnC = findViewById(R.id.btn_completable);
        btnCT = findViewById(R.id.btn_completable_then);
        btnT = findViewById(R.id.btn_completable_to_observable);

        btn.setText(this.getClass().getSimpleName());
        textView = findViewById(R.id.textView);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSomeWork();
            }
        });

        btnC.setOnClickListener(view -> testCompletable());
        btnCT.setOnClickListener(view -> testCompletableToObservable2().subscribe());
        btnT.setOnClickListener(view -> testCompletableToObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(o -> {
            System.out.println("o:" + o);
            System.out.println("o:" + o.getClass());
            System.out.println("o:" + o.toString());
        }).doOnComplete(() -> {
            System.out.println("btnT doOnComplete");
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("btnT doOnError");
            }
        }).subscribe());
    }

    /*
     * simple example using CompletableObserver
     */
    private void doSomeWork() {
        Completable completable = Completable.timer(1000, TimeUnit.MILLISECONDS);

        completable.subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread()).subscribe(getCompletableObserver());
    }

    private CompletableObserver getCompletableObserver() {
        return new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onComplete");
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.d(TAG, " onError : " + e.getMessage());
            }
        };
    }


    private void testCompletable() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                System.out.println("Hello World");
            }
        }).subscribe();
    }

    @SuppressLint("CheckResult")
    private void testCompletableThen() {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(@NonNull CompletableEmitter emitter) throws Exception {

                try {
                    TimeUnit.SECONDS.sleep(1);
                    emitter.onComplete();
                } catch (InterruptedException e) {
                    emitter.onError(e);
                }
            }
        }).andThen(Observable.range(1, 10)).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println(integer);
            }
        });
    }

    private Observable<Boolean> testCompletableToObservable() {
        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("Hello World");
            }
        }).toObservable();
    }


    @SuppressLint("CheckResult")
    private Observable<Object> testCompletableToObservable2() {
        return Completable.create(emitter -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                emitter.onComplete();
            } catch (InterruptedException e) {
                emitter.onError(e);
            }
        }).toObservable();
    }
}