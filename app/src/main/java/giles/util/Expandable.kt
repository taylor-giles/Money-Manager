package giles.util

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE

interface Expandable{
    val content: View?
    var expanded: Boolean


    /**
     * Puts the view in the expanded state, showing the content
     */
    fun expand(){
        expanded = true
        if(content != null){
            content!!.visibility = VISIBLE
        }
    }


    /**
     * Puts the view in the collapsed state, hiding the content
     */
    fun collapse(){
        expanded = false
        if(content != null){
            content!!.visibility = GONE
        }
    }
}


/**
 * A basic implementation of the [Expandable] interface
 */
class BasicExpandableView(
    override val content: View?,
    override var expanded: Boolean = false,
    button: View? = null
) : Expandable {
    init{
        expanded = !expanded
        toggleState()
        button?.setOnClickListener { toggleState() }
    }

    fun toggleState(){
        if(expanded){
            collapse()
        } else {
            expand()
        }
    }
}