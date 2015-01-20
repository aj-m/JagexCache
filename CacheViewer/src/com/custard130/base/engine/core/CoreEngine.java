package com.custard130.base.engine.core;

import com.custard130.base.engine.Debug;
import com.custard130.base.engine.core.input.Input;
import com.custard130.base.engine.rendering.RenderingEngine;
import com.custard130.base.engine.rendering.Window;

public final class CoreEngine {
	private boolean isRunning;
	private Game game;
	private RenderingEngine renderingEngine;
	private int width;
	private int height;
	private final double frameTime;

	public CoreEngine(int width, int height, double framerate, Game game) {
		this.isRunning = false;
		this.game = game;
		this.width = width;
		this.height = height;
		this.frameTime = 1.0 / framerate;
		game.setEngine(this);
	}

	public void createWindow(String title, boolean fullscreen) {
		Window.createWindow(width, height, title);
		this.renderingEngine = new RenderingEngine();
		if(fullscreen){
			Window.makeFullscreen(fullscreen);
		}

	}

	public void start() {
		if (isRunning)
			return;

		run();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}

	private void run() {
		isRunning = true;

		int frames = 0;
		long frameCounter = 0;

		game.init();

		double lastTime = Time.getTime();
		double unprocessedTime = 0;

		while (isRunning) {
			boolean render = false;

			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;

			unprocessedTime += passedTime;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;

				unprocessedTime -= frameTime;

				if (Window.isCloseRequested())
					stop();

				game.input((float) frameTime);
				Input.update();

				game.update((float) frameTime);

				if (frameCounter >= 1.0) {
					Debug.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				game.render(renderingEngine);
				Window.render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		cleanUp();
	}

	private void cleanUp() {
		Window.dispose();
	}

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}
}