package c.giles.budgetappv11.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class BudgetView extends View {
    public BudgetView(Context context) {
        super(context);
        init(null);
    }

    public BudgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public BudgetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public BudgetView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set){

    }


}
