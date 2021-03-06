/**
 * Copyright 2016 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bartoszlipinski.rxanimationbinding;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;

import rx.Observable;
import rx.Subscriber;

@TargetApi(Build.VERSION_CODES.KITKAT)
final class AnimatorPauseListenerOnSubscribe implements Observable.OnSubscribe<Animator> {

    private final Animator animator;
    private final int eventToCallOn;

    AnimatorPauseListenerOnSubscribe(Animator animator, int eventToCallOn) {
        this.animator = animator;
        this.eventToCallOn = eventToCallOn;
    }

    @Override
    public void call(final Subscriber<? super Animator> subscriber) {

        final Animator.AnimatorPauseListener listener = new Animator.AnimatorPauseListener() {
            @Override
            public void onAnimationPause(Animator animator) {
                if (eventToCallOn == AnimationEvent.PAUSE && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(animator);
                }
            }

            @Override
            public void onAnimationResume(Animator animator) {
                if (eventToCallOn == AnimationEvent.RESUME && !subscriber.isUnsubscribed()) {
                    subscriber.onNext(animator);
                }
            }
        };

        animator.addPauseListener(listener);

        subscriber.add(new OnUnsubscribedCallback() {
            @Override
            protected void onUnsubscribe() {
                animator.removePauseListener(listener);
            }
        });
    }
}
