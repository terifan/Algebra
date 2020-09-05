package org.terifan.algebra;


/**
 * This class represents a position, scale and orientation in 3-space.
 */
public class Transform3D
{
	protected final static double PI2 = 2 * Math.PI;

	protected boolean mHasDirection;
	protected boolean mHasScale;
	protected final Quaternion mQuaternion;
	protected final Vec3d mPosition;
	protected final Vec3d mScale;


	/**
	 * Constructs a new Transform3D object with position and direction set
	 * to (0,0,0) and scale to (1,1,1).
	 */
	public Transform3D()
	{
		mQuaternion = new Quaternion();
		mPosition = new Vec3d(0, 0, 0);
		mScale = new Vec3d(1, 1, 1);
	}


	public Transform3D(double... aPositonRotationScale)
	{
		this();
		
		assert aPositonRotationScale.length == 3 || aPositonRotationScale.length == 6 || aPositonRotationScale.length == 9;
		
		setPosition(aPositonRotationScale[0], aPositonRotationScale[1], aPositonRotationScale[2]);
		if (aPositonRotationScale.length >= 6)
		{
			setDirection(aPositonRotationScale[3], aPositonRotationScale[4], aPositonRotationScale[5]);
		}
		if (aPositonRotationScale.length >= 9)
		{
			setScale(aPositonRotationScale[6], aPositonRotationScale[7], aPositonRotationScale[8]);
		}
	}


	/**
	 * Constructs a new Transform3D object with position set to the position
	 * provided and direction set to (0,0,0) and scale to (1,1,1).
	 *
	 * Data is copied. No references is kept.
	 */
	public Transform3D(Vec3d aPosition)
	{
		this();

		setPosition(aPosition);
	}


	/**
	 * Constructs a new Transform3D object with position and direction set to
	 * those provided and scale to (1,1,1).
	 *
	 * Data is copied. No references is kept.
	 */
	public Transform3D(Vec3d aPosition, Vec3d aDirection)
	{
		this();

		setPosition(aPosition);
		setDirection(aDirection);
	}


	/**
	 * Constructs a new Transform3D object with position, direction and scale
	 * set to those provided and scale to (1,1,1).
	 *
	 * Data is copied. No references is kept.
	 */
	public Transform3D(Vec3d aPosition, Vec3d aDirection, Vec3d aScale)
	{
		this();

		setPosition(aPosition);
		setDirection(aDirection);
		setScale(aScale);
	}


	/**
	 * Returns true if any scale component is different from 1.
	 */
	public boolean hasScale()
	{
		return mHasScale;
	}


	/**
	 * Returns true if any direction component is different from 0.
	 */
	public boolean hasDirection()
	{
		return mHasDirection;
	}


	/**
	 * Gets the scale vector of this Transform3D object.
	 */
	public Vec3d getScale()
	{
		return mScale;
	}


	/**
	 * Sets the scale of this Transform3D object to the absolute value provided.
	 *
	 * @param aScale
	 *    a scale value.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setScale(double aScale)
	{
		mScale.set(aScale, aScale, aScale);
		mHasScale = aScale != 1;
		return this;
	}


	/**
	 * Sets the scale of this Transform3D object to the absolute values provided.
	 *
	 * @param aScaleX
	 *    a scale value.
	 * @param aScaleY
	 *    a scale value.
	 * @param aScaleZ
	 *    a scale value.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setScale(double aScaleX, double aScaleY, double aScaleZ)
	{
		mScale.set(aScaleX, aScaleY, aScaleZ);
		mHasScale = aScaleX != 1 || aScaleY != 1 || aScaleZ != 1;
		return this;
	}


	/**
	 * Sets the scale of this Transform3D object to the absolute values provided.
	 * The provided vector is copied to the internal vector of this Transform3D object.
	 *
	 * Data is copied. No references is kept.
	 *
	 * @param aScale
	 *    a scale vector.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setScale(Vec3d aScale)
	{
		mScale.set(aScale);
		mHasScale = mScale.x != 1 || mScale.y != 1 || mScale.z != 1;
		return this;
	}


	/**
	 * Multiplies each xyz component of the internal scale value with the value provided.
	 *
	 * @param aScale
	 *    a scale vector.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D scale(Vec3d aScale)
	{
		mScale.multiply(aScale);
		mHasScale = mScale.x != 1 || mScale.y != 1 || mScale.z != 1;
		return this;
	}


	/**
	 * Multiplies each xyz component of the internal scale value with the value provided.
	 *
	 * @param aScale
	 *    scale value.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D scale(double aScale)
	{
		mScale.multiply(aScale);
		mHasScale = mScale.x != 1 || mScale.y != 1 || mScale.z != 1;
		return this;
	}


	/**
	 * Multiplies each xyz component of the internal scale value with the value provided.
	 *
	 * @param aScaleX
	 *    scale value.
	 * @param aScaleY
	 *    scale value.
	 * @param aScaleZ
	 *    scale value.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D scale(double aScaleX, double aScaleY, double aScaleZ)
	{
		mScale.multiply(aScaleX, aScaleY, aScaleZ);
		mHasScale = mScale.x != 1 || mScale.y != 1 || mScale.z != 1;
		return this;
	}


	/**
	 * Gets the direction of this Transform3D object.
	 */
	public Vec3d getDirection()
	{
		return mQuaternion.getDirection();
	}


	/**
	 * Sets the direction of this Transform3D object to the absolute values provided.
	 *
	 * @param aDirectionX
	 *    a normalized direction.
	 * @param aDirectionY
	 *    a normalized direction.
	 * @param aDirectionZ
	 *    a normalized direction.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setDirection(double aDirectionX, double aDirectionY, double aDirectionZ)
	{
		mQuaternion.setDirection(aDirectionX, aDirectionY, aDirectionZ);
		mHasDirection = aDirectionX != 1 || aDirectionY != 1 || aDirectionZ != 1;
		return this;
	}


	/**
	 * Sets the direction of this Transform3D object to the absolute values provided.
	 *
	 * @param aDirectionX
	 *    a normalized direction.
	 * @param aDirectionY
	 *    a normalized direction.
	 * @param aDirectionZ
	 *    a normalized direction.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setDirectionDegrees(double aDirectionX, double aDirectionY, double aDirectionZ)
	{
		mQuaternion.setDirection(aDirectionX/360.0, aDirectionY/360.0, aDirectionZ/360.0);
		mHasDirection = aDirectionX != 1 || aDirectionY != 1 || aDirectionZ != 1;
		return this;
	}


	/**
	 * Sets the direction of this Transform3D object to the absolute values provided.
	 * The provided vector is copied to the internal vector of this Transform3D object.
	 *
	 * Data is copied. No references is kept.
	 *
	 * @param aDirection
	 *    a normalized direction vector.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setDirection(Vec3d aDirection)
	{
		mQuaternion.setDirection(aDirection);
		mHasDirection = aDirection.x != 1 || aDirection.y != 1 || aDirection.z != 1;
		return this;
	}


	/**
	 * Adds the direction vector provided to the direction of this Transform3D object.
	 *
	 * @param aDirectionX
	 *    a normalized Direction.
	 * @param aDirectionY
	 *    a normalized Direction.
	 * @param aDirectionZ
	 *    a normalized Direction.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D rotate(double aDirectionX, double aDirectionY, double aDirectionZ)
	{
		mQuaternion.rotate(aDirectionX, aDirectionY, aDirectionZ);
		mHasDirection = mQuaternion.getDirection().x != 1 || mQuaternion.getDirection().y != 1 || mQuaternion.getDirection().z != 1;
		return this;
	}


	/**
	 * Adds the direction vector provided to the direction of this Transform3D object.
	 *
	 * @param aDirectionX
	 *    a normalized Direction.
	 * @param aDirectionY
	 *    a normalized Direction.
	 * @param aDirectionZ
	 *    a normalized Direction.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D rotateDegrees(double aDirectionX, double aDirectionY, double aDirectionZ)
	{
		mQuaternion.rotate(aDirectionX/360.0, aDirectionY/360.0, aDirectionZ/360.0);
		mHasDirection = mQuaternion.getDirection().x != 1 || mQuaternion.getDirection().y != 1 || mQuaternion.getDirection().z != 1;
		return this;
	}


	/**
	 * Adds the direction vector provided to the Direction of this Transform3D object.
	 *
	 * @param aDirection
	 *    a normalized Direction vector.
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D rotate(Vec3d aDirection)
	{
		mQuaternion.rotate(aDirection);
		mHasDirection = mQuaternion.getDirection().x != 1 || mQuaternion.getDirection().y != 1 || mQuaternion.getDirection().z != 1;
		return this;
	}


	/**
	 * Gets the position of this Transform3D object.
	 */
	public Vec3d getPosition()
	{
		return mPosition;
	}


	/**
	 * Moves the Transform3D object to the absolute coordinate provided.
	 *
	 * @param aPositionX
	 *    x position
	 * @param aPositionY
	 *    y position
	 * @param aPositionZ
	 *    z position
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setPosition(double aPositionX, double aPositionY, double aPositionZ)
	{
		mPosition.set(aPositionX, aPositionY, aPositionZ);
		return this;
	}


	/**
	 * Moves the Transform3D object to the absolute coordinate provided. The
	 * provided vector is copied to the internal vector of this Transform3D object.
	 *
	 * @param aPosition
	 *    a position
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D setPosition(Vec3d aPosition)
	{
		mPosition.set(aPosition);
		return this;
	}


	/**
	 * Moves the Transform3D relative to the position it already has.
	 *
	 * @param aPositionX
	 *    x position
	 * @param aPositionY
	 *    y position
	 * @param aPositionZ
	 *    z position
	 * @return
	 *    the Transform3D object
	 */
	public Transform3D translate(double aPositionX, double aPositionY, double aPositionZ)
	{
		mPosition.add(aPositionX, aPositionY, aPositionZ);
		return this;
	}


	/**
	 * Moves the Transform3D relative to the position it already has.
	 *
	 * @param aPosition
	 *    a position
	 * @return
	 *    the Transform3D object.
	 */
	public Transform3D translate(Vec3d aPosition)
	{
		mPosition.add(aPosition);
		return this;
	}


	/**
	 * Moves the Transform3D object along it's direction. The Transform3D
	 * object will move backwards if the aDistance value is negative.
	 *
	 * @param aDistance
	 *    the distance to move the Transform3D.
	 */
	public Transform3D forward(double aDistance)
	{
		if (aDistance != 0)
		{
			Vec3d direction = mQuaternion.getDirection();

			mPosition.x += aDistance * Math.cos(PI2 * direction.x) * Math.cos(PI2 * (direction.y + 0.25f));
			mPosition.y += aDistance * Math.sin(PI2 * direction.x);
			mPosition.z += aDistance * Math.cos(PI2 * direction.x) * Math.sin(PI2 * (direction.y + 0.25f));
		}
		
		return this;
	}


	/**
	 * Moves the Transform3D object side ways. A positive value will move the
	 * Transform3D object right and a negative value will move left.
	 *
	 * @param aDistance
	 *    the distance to move the Transform3D.
	 */
	public void strafe(double aDistance)
	{
		if (aDistance != 0)
		{
			Vec3d direction = mQuaternion.getDirection();

			double rotY = direction.y + (aDistance > 0 ? +0.25 : -0.25);
			if (rotY > 1) rotY--;
			if (rotY < 0) rotY++;

			double rotZ = direction.z;

			mPosition.x += Math.abs(aDistance) * Math.sin(PI2 * rotY);
			mPosition.y -=          aDistance  * Math.sin(PI2 * rotZ);
			mPosition.z -= Math.abs(aDistance) * Math.cos(PI2 * rotY);
		}
	}


	/**
	 * Rotates the Transform3D object along it's X.
	 */
	public void pitch(double aAmount)
	{
		if (aAmount != 0)
		{
			rotate(aAmount, 0, 0);
		}
	}


	/**
	 * Rotates the Transform3D object along it's Y.
	 */
	public void yaw(double aAmount)
	{
		if (aAmount != 0)
		{
			rotate(0, aAmount, 0);
		}
	}


	/**
	 * Rotates the Transform3D object along it's Z.
	 */
	public void roll(double aAmount)
	{
		if (aAmount != 0)
		{
			rotate(0, 0, aAmount);
		}
	}


	/**
	 * Gets the Quaternion object which hold the direction of this Transform3D object.
	 */
	public Quaternion getQuaternion()
	{
		return mQuaternion;
	}


	/**
	 * Transforms a vector using this Transform3D object. The vector provided
	 * is updated with the new transformed coordinate.
	 *
	 * The vector is rotated and moved and then finally scaled.
	 */
	public Vec3d transform(Vec3d aVector)
	{
		if (mHasScale)
		{
			aVector.multiply(mScale);
		}

		if (mHasDirection)
		{
			mQuaternion.transform(aVector);
		}

		aVector.add(mPosition);
		
		return aVector;
	}


	/**
	 * Transforms an array of vector using this Transform3D object. The vectors
	 * provided is updated with the new transformed coordinate.
	 *
	 * The vector is rotated and then moved and finally scaled.
	 */
	public void transform(Vec3d [] aVertices, int aCoordinateCount)
	{
		if (mHasScale)
		{
			for (int i = aCoordinateCount; --i >= 0;)
			{
				aVertices[i].multiply(mScale);
			}
		}

		if (mHasDirection)
		{
			for (int i = aCoordinateCount; --i >= 0;)
			{
				mQuaternion.transform(aVertices[i]);
			}
		}

		for (int i = aCoordinateCount; --i >= 0;)
		{
			aVertices[i].add(mPosition);
		}
	}


	/**
	 * Transforms a vector using this Transform3D object. The vector provided
	 * is updated with the new transformed coordinate.
	 *
	 * This method only rotates the vector provided, without any displacements.
	 */
	public void transformDirection(Vec3d aVector)
	{
		mQuaternion.transform(aVector);
	}


	/**
	 * Transforms an array of vectors using this Transform3D object. The vector
	 * provided is updated with the new transformed coordinate.
	 *
	 * This method only rotates the vector provided, without any displacements.
	 */
	public void transformDirection(Vec3d [] aVertices)
	{
		for (int i = aVertices.length; --i >= 0;)
		{
			mQuaternion.transform(aVertices[i]);
		}
	}


	public void transformPosition(Vec3d aVector)
	{
		aVector.add(mPosition);
	}


	public void transformPosition(Vec3d [] aVertices)
	{
		for (int i = aVertices.length; --i >= 0;)
		{
			aVertices[i].add(mPosition);
		}
	}


	/**
	 * Returns a description of this object.
	 */
	@Override
	public String toString()
	{
		return "[position=" + mPosition + ", direction=" + mQuaternion.getDirection() + ", scale=" + mScale + "]";
	}


	/**
	 * Constructs a clone of this Transform3D.
	 */
	@Override
	public Transform3D clone()
	{
		return copyTo(new Transform3D());
	}


	/**
	 * Duplicates all properties of this object to the object provided.
	 */
	public Transform3D copyTo(Transform3D aTransform) throws ClassCastException
	{
		aTransform.setPosition(mPosition);
		aTransform.setDirection(mQuaternion.getDirection());
		aTransform.setScale(mScale);
//		aTransform.setDisplacement(mDisplacement);

		return aTransform;
	}
}