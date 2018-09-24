package sculpture;

public class Utiles extends java.lang.Object {
	/** Utiles */
	public static final java.util.Random RANDOM=new java.util.Random();
	public static boolean choix() {
		return RANDOM.nextBoolean();
	}
	public static boolean choix(final double prop) {
		return RANDOM.nextFloat() < prop;
	}
	public static float random() {
		return RANDOM.nextFloat();
	}
	public static float random(final double min,final double max) {
		return (float)(min + (max - min) * RANDOM.nextDouble());
	}
	public static int random(final int lim) {
		return RANDOM.nextInt(lim);
	}
	public static int random(final int min,final int max) {
		return min + RANDOM.nextInt(max - min + 1);
	}
}
