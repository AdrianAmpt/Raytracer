package nl.oikos.raytracter.render;

public interface StatusIndicator
{
	void setElapsed(long millis);

	void setEtr(long millis);

	void setStatusMessage(String message);

	void onRenderCompleted();
}
