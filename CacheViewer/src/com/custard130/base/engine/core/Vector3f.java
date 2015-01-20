package com.custard130.base.engine.core;

public class Vector3f{
	private float	x;
	private float	y;
	private float	z;
	
	public Vector3f(float f) {
		this(f, f, f);
	}
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f r) {
		set(r.x, r.y, r.z);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}
	
	public float dot(Vector3f r) {
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	public void set(float x, float y, float z) {
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public void set(Vector3f r) {
		set(r.x, r.y, r.z);
	}
	
	public float max() {
		return Math.max(x, Math.max(y, z));
	}
	
	public Vector3f max(Vector3f other) {
		Vector3f res = new Vector3f(0);
		res.x = Math.max(x, other.x);
		res.y = Math.max(y, other.y);
		res.z = Math.max(z, other.z);
		return res;
	}
	
	public Vector3f cross(Vector3f r) {
		float x_ = y * r.getZ() - z * r.getY();
		float y_ = z * r.getX() - x * r.getZ();
		float z_ = x * r.getY() - y * r.getX();
		
		return new Vector3f(x_, y_, z_);
	}
	
	public Vector3f normalized() {
		float length = length();
		
		return new Vector3f(x / length, y / length, z / length);
	}
	
	public Vector3f rotate(Quaternion rotation) {
		Quaternion conjugate = rotation.conjugate();
		Quaternion w = rotation.mul(this).mul(conjugate);
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f rotate(Vector3f axis, float angle) {
		return rotate(new Quaternion().initRotation(axis, angle));
	}
	
	public Vector3f lerp(Vector3f dest, float lerpFactor) {
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	public Vector3f add(Vector3f r) {
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	public Vector3f add(float r) {
		return new Vector3f(x + r, y + r, z + r);
	}
	
	public Vector3f sub(Vector3f r) {
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}
	
	public Vector3f sub(float r) {
		return new Vector3f(x - r, y - r, z - r);
	}
	public Vector3f sub(float rx, float ry, float rz) {
		return new Vector3f(x - rx, y - ry, z - rz);
	}
	
	public Vector3f mul(Vector3f r) {
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}
	
	public Vector3f mul(float r) {
		return new Vector3f(x * r, y * r, z * r);
	}
	
	public Vector3f div(Vector3f r) {
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}
	
	public Vector3f div(float r) {
		return new Vector3f(x / r, y / r, z / r);
	}
	
	public Vector3f abs() {
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	public String toString() {
		return "(" + x + " " + y + " " + z + ")";
	}
	
	public Vector2f getXY() {
		return new Vector2f(x, y);
	}
	
	public Vector2f getYZ() {
		return new Vector2f(y, z);
	}
	
	public Vector2f getZX() {
		return new Vector2f(z, x);
	}
	
	public Vector2f getYX() {
		return new Vector2f(y, x);
	}
	
	public Vector2f getZY() {
		return new Vector2f(z, y);
	}
	
	public Vector2f getXZ() {
		return new Vector2f(x, z);
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
	
	public boolean equals(Vector3f r) {
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}
	
	public Vector3f mul(float x, float y, float z) {
		return mul(new Vector3f(x, y, z));
	}


}
