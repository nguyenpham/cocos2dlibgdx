package org.cclgdx.actions.interval;

import org.cclgdx.actions.base.CCFiniteTimeAction;
import org.cclgdx.nodes.CCNode;

//
// Repeat
//

/**
 * Repeats an action a number of times. To repeat an action forever use the
 * CCRepeatForever action.
 */
public class CCRepeat extends CCIntervalAction {
	private final int times;
	private int total;
	private final CCFiniteTimeAction other;

	/**
	 * creates a CCRepeat action. Times is an unsigned integer between 1 and
	 * pow(2,30)
	 */
	public static CCRepeat action(CCFiniteTimeAction action, int t) {
		return new CCRepeat(action, t);
	}

	/**
	 * initializes a CCRepeat action. Times is an unsigned integer between 1 and
	 * pow(2,30)
	 */
	protected CCRepeat(CCFiniteTimeAction action, int t) {
		super(action.getDuration() * t);

		times = t;
		other = action;

		total = 0;
	}

	@Override
	public CCIntervalAction copy() {
		return new CCRepeat(other.copy(), times);
	}

	@Override
	public void start(CCNode aTarget) {
		total = 0;
		super.start(aTarget);
		other.start(aTarget);
	}

	@Override
	public void stop() {

		other.stop();
		super.stop();
	}

	@Override
	public void update(float dt) {
		float t = dt * times;
		if (t > total + 1) {
			other.update(1.0f);
			total++;
			other.stop();
			other.start(target);
			// repeat is over ?
			if (total == times)
				// so, set it in the original position
				other.update(0);
			else {
				// no ? start next repeat with the right update
				// to prevent jerk (issue #390)
				other.update(t - total);
			}

		} else {
			float r = t % 1.0f;
			// fix last repeat position
			// else it could be 0.
			if (dt == 1.0f) {
				r = 1.0f;
				total++;
			}
			other.update(Math.min(r, 1));
		}
	}

	@Override
	public boolean isDone() {
		return (total == times);
	}

	@Override
	public CCIntervalAction reverse() {
		return new CCRepeat(other.reverse(), times);
	}
}
