package edu.nr.lib;

import java.util.ArrayList;

import com.kauailabs.sf2.interpolation.IInterpolate;
import com.kauailabs.sf2.quantity.ICopy;
import com.kauailabs.sf2.quantity.IQuantity;
import com.kauailabs.sf2.quantity.Scalar;
import com.kauailabs.sf2.units.Unit;
import com.kauailabs.sf2.units.Unit.IUnit;

/**
 * 
 * An Encoder Position class, to be used by EncoderHistory.
 * 
 * It works in feet.
 *
 */
public class EncoderPosition implements IInterpolate<EncoderPosition>, ICopy<EncoderPosition>, IQuantity {

	private float x;

	class FloatVectorStruct {
		float x;
	};

	/**
	 * Constructs a Encoder instance, with an initial position of zero
	 */
	public EncoderPosition() {
		set(0);
	}

	/**
	 * Constructs a Encoder instance, using values from another Encoder
	 * instance.
	 * 
	 * @param src
	 *            - the Encoder instance used to initialize this Encoder.
	 */
	public EncoderPosition(final EncoderPosition src) {
		set(src);
	}

	/**
	 * Constructs a Encoder instance, using the provided x value.
	 * 
	 * @param x
	 *            - the position value.
	 */
	public EncoderPosition(float x) {
		set(x);
	}

	/**
	 * Modifies the Encoder by setting the position.
	 * 
	 * @param x
	 *            - the position.
	 */
	public void set(float x) {
		this.x = x;
	}

	/**
	 * Modifes the Encoder to be equal to the provided Encoder.
	 * 
	 * @param src
	 *            - the Encoder instance used to initialize this Encoder.
	 */
	public void set(final EncoderPosition src) {
		set(src.x);
	}

	/**
	 * Accessor for the Encoder's position.
	 * 
	 * @return Encoder position
	 */
	public float getPosition() {
		return x;
	}

	@Override
	public void interpolate(EncoderPosition to, double time_ratio, EncoderPosition out) {
		if(this.x == to.x) {
			out = new EncoderPosition(this);
			return;
		}
		
		out = new EncoderPosition((float) (to.x * time_ratio + this.x * (1 - time_ratio)));
	}

	@Override
	public void copy(EncoderPosition t) {
		this.x = t.x;
	}

	@Override
	public EncoderPosition instantiate_copy() {
		return new EncoderPosition(this);
	}

	static public IUnit[] getUnits() {
		return new IUnit[] { new Unit().new Distance().new Meters() };
	}

	@Override
	public boolean getPrintableString(StringBuilder printable_string) {
		return false;
	}

	@Override
	public boolean getContainedQuantities(ArrayList<IQuantity> quantities) {
		quantities.add(new Scalar(x));
		return true;
	}

	@Override
	public boolean getContainedQuantityNames(ArrayList<String> quantity_names) {
		quantity_names.add("X");
		return true;
	}
	
}
