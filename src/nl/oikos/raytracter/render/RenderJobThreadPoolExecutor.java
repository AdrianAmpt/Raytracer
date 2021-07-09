package nl.oikos.raytracter.render;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Adrian on 19-8-2017.
 */
public class RenderJobThreadPoolExecutor extends ThreadPoolExecutor
{
	private boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();
	private Condition unPaused = pauseLock.newCondition();

	public RenderJobThreadPoolExecutor(int corePoolSize,
									   int maximumPoolSize,
									   long keepAliveTime,
									   TimeUnit unit,
									   BlockingQueue<Runnable> workQueue)
	{
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	protected void beforeExecute(Thread t, Runnable r)
	{
		super.beforeExecute(t, r);
		pauseLock.lock();
		try
		{
			while (isPaused) unPaused.await();
		}
		catch (InterruptedException ie)
		{
			t.interrupt();
		}
		finally
		{
			pauseLock.unlock();
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		super.afterExecute(r, t);

		if (isPaused)
		{
			if (r instanceof RenderJob)
				this.execute(new RenderJob((RenderJob) r));
		}

		if (this.getTaskCount() - this.getCompletedTaskCount() == 1)
			this.shutdown();
	}

	public void pause()
	{
		pauseLock.lock();
		try
		{
			isPaused = true;
		}
		finally
		{
			pauseLock.unlock();
		}
	}

	public void resume()
	{
		pauseLock.lock();
		try
		{
			isPaused = false;
			unPaused.signalAll();
		}
		finally
		{
			pauseLock.unlock();
		}
	}

	public boolean isPaused()
	{
		return isPaused;
	}
}
