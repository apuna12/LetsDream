import android.R.id
import android.R.layout
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

import java.util.ArrayList

import android.R.id.checkbox
import sk.letsdream.R


class MyAdapter(private val mContext: Context, resource: Int, objects: List<StateVO>) :
    ArrayAdapter<StateVO>(mContext, resource, objects) {
    private val listState: ArrayList<StateVO>
    private val myAdapter: MyAdapter
    private var isFromView = false

    init {
        this.listState = objects as ArrayList<StateVO>
        this.myAdapter = this
    }

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            val layoutInflator = LayoutInflater.from(mContext)
            convertView = layoutInflator.inflate(R.layout.spinner_layout, null)
            holder = ViewHolder()
            holder.mTextView = convertView!!
                .findViewById(R.id.text) as TextView
            holder.mCheckBox = convertView
                .findViewById(R.id.checkbox) as CheckBox
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.mTextView!!.setText(listState[position].title)

        // To check weather checked event fire from getview() or user input
        isFromView = true
        holder.mCheckBox!!.isChecked = listState[position].isSelected
        isFromView = false

        if (position == 0) {
            holder.mCheckBox!!.visibility = View.INVISIBLE
        } else {
            holder.mCheckBox!!.visibility = View.VISIBLE
        }
        holder.mCheckBox!!.tag = position
        holder.mCheckBox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            val getPosition = buttonView.tag as Int

            if (!isFromView) {
                Log.e("isFromView", (!isFromView).toString() + "")
                if (getPosition == 1) { // cam make a rule according to it for any position
                    if (!listState[1].isSelected) {
                        if (isChecked) {
                            for (i in 1 until listState.size) {
                                listState[i].isSelected = true
                            }
                        }
                        else
                        {
                            for (i in 1 until listState.size) {
                                listState[i].isSelected = false
                            }
                        }

                    } else {
                        if (!isChecked) {
                            for (i in 1 until listState.size) {
                                listState[i].isSelected = false
                            }
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            }
        }
        return convertView
    }

    private inner class ViewHolder {
        var mTextView: TextView? = null
        var mCheckBox: CheckBox? = null
    }
}