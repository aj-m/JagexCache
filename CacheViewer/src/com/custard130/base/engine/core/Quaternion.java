package com.custard130.base.engine.core;

import static java.lang.Math.*;

public class Quaternion {
	private float	x;
	private float	y;
	private float	z;
	private float	w;
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternion(Quaternion r) {
		set(r.x, r.y, r.z, r.w);
	}
	
	public Quaternion() {
		this(0, 0, 0, 1);
	}
	
	public Quaternion(Vector3f axis, float angle) {
		float sinHalfAngle = (float) Math.sin(angle / 2);
		float cosHalfAngle = (float) Math.cos(angle / 2);
		
		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = cosHalfAngle;
	}
	
	public Quaternion initRotation(Vector3f axis, float angle) {
		return new Quaternion(axis, angle);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion normalized() {
		float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	public Quaternion mul(float x,float y,float z,float w) {
		return mul(new Quaternion(x, y, z, w));
		
	}
	public Quaternion mul(float x,float y,float z) {
		return mul(new Vector3f(x, y, z));
		
	}
	public Quaternion mul(Quaternion r) {
		float w_ = w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Quaternion mul(Vector3f r) {
		float w_ = -x * r.getX() - y * r.getY() - z * r.getZ();
		float x_ = w * r.getX() + y * r.getZ() - z * r.getY();
		float y_ = w * r.getY() + z * r.getX() - x * r.getZ();
		float z_ = w * r.getZ() + x * r.getY() - y * r.getX();
		
		return new Quaternion(x_, y_, z_, w_);
	}
	
	public Matrix4f toRotationMatrix() {
		// Vector3f forward = new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z
		// + w * x), 1.0f - 2.0f * (x * x + y * y));
		// Vector3f up = new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x *
		// x + z * z), 2.0f * (y * z - w * x));
		// Vector3f right = new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f *
		// (x * y - w * z), 2.0f * (x * z + w * y));
		
		return new Matrix4f().initRotation(this);
	}
	
	public Vector3f getForward() {
		// return new Vector3f(0, 0, 1).rotate(this);
		return new Vector3f(2.0f * (x * z - w * y), 2.0f * (y * z + w * x), 1.0f - 2.0f * (x * x + y * y));
	}
	
	public Vector3f getUp() {
		// return new Vector3f(0, 1, 0).rotate(this);
		return new Vector3f(2.0f * (x * y + w * z), 1.0f - 2.0f * (x * x + z * z), 2.0f * (y * z - w * x));
	}
	
	public Vector3f getRight() {
		// return new Vector3f(1, 0, 0).rotate(this);
		return new Vector3f(1.0f - 2.0f * (y * y + z * z), 2.0f * (x * y - w * z), 2.0f * (x * z + w * y));
	}
	
	public Quaternion add(Quaternion r) {
		return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
	}
	
	public Quaternion add(float r) {
		return new Quaternion(x + r, y + r, z + r, w + r);
	}
	
	public Quaternion sub(Quaternion r) {
		return new Quaternion(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
	}
	
	public Quaternion sub(float r) {
		return new Quaternion(x - r, y - r, z - r, w - r);
	}
	
	public Quaternion mul(float r) {
		return new Quaternion(x * r, y * r, z * r, w * r);
	}
	
	public float dot(Quaternion r) {
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}
	
	public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest) {
		Quaternion correctedDest = dest;
		if (shortest && this.dot(dest) < 0) {
			correctedDest = dest.mul(-1);
		}
		return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
	}
	
	public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest) {
		final float EPSILON = 1e3f;
		
		float cos = this.dot(dest);
		Quaternion correctedDest = dest;
		if (shortest && cos < 0) {
			cos = -cos;
			correctedDest = dest.mul(-1);
		}
		if (abs(cos) >= 1 - EPSILON) return nlerp(correctedDest, lerpFactor, false);
		float sin = (float) sqrt(1.0f - cos * cos);
		float angle = (float) atan2(sin, cos);
		float invSin = 1.0f / sin;
		
		float srcFactor = (float) sin((1.0f - lerpFactor) * angle) * invSin;
		float destFactor = (float) sin((lerpFactor) * angle) * invSin;
		
		return this.mul(srcFactor).add(correctedDest.mul(destFactor));
	}
	
	@Override
	public boolean equals(Object o) {
		return equals((Quaternion) o);
	}
	

	public Quaternion(Matrix4f rot) {
		float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);
		if (trace > 0) {
			float s = 0.5f / (float) Math.sqrt(trace + 1.0f);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		} else {
			if (rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			} else if (rot.get(1, 1) > rot.get(2, 2)) {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			} else {
				float s = 2.0f * (float) Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0)) / s;
				x = (rot.get(2, 0) + rot.get(0, 2)) / s;
				y = (rot.get(1, 2) + rot.get(2, 1)) / s;
				z = 0.25f * s;
			}
		}
		float length = (float) Math.sqrt(x * x + y * y + z * z + w * w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}
	
	public boolean equals(Quaternion r) {
		if (x == r.getX()) return false;
		if (y == r.getY()) return false;
		if (z == r.getZ()) return false;
		if (w == r.getW()) return false;
		return true;
	}
	
	public void set(float x, float y, float z, float w) {
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public void set(Quaternion r) {
		set(r.x, r.y, r.z, r.w);
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getZ() {
		return z;
	}
	
	public void setZ(float z) {
		this.z = z;
	}
	
	public float getW() {
		return w;
	}
	
	public void setW(float w) {
		this.w = w;
	}
}
