package uff.br.infouffdtn.interfacepk;

import uff.br.infouffdtn.R;
import uff.br.infouffdtn.R.anim;
import android.app.Activity;

public class SlideTransition {
	public static void forwardTransition(Activity act){
		act.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	public static void backTransition(Activity act){
		act.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}
}
