package nl.oikos.raytracter.util;

/**
 * Created by Adrian on 23-8-2017.
 */
public class Reference<T>
{
	private T reference;

	public Reference(T initialValue)
	{
		reference = initialValue;
	}

	public void set(T newVal)
	{
		reference = newVal;
	}

	public T get()
	{
		return reference;
	}
}