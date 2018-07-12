package com.example.lulin.todolist;

import android.view.animation.LinearInterpolator;

public class Interpolator extends LinearInterpolator {
	private float factor;

	public Interpolator() {
		this.factor = 0.15f;
	}

	@Override
	public float getInterpolation(float input) {
		return (float) (Math.pow(2, -10 * input)
				* Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
	}
}
