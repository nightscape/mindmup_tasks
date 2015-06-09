package com.mindmup.android.tasks

import android.app._
import android.content.{ Intent, IntentSender, Context, SharedPreferences }
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.view._
import android.widget._
import macroid._
import macroid.contrib._
import macroid.FullDsl._
import macroid.IdGeneration
import macroid.viewable._
import macroid.contrib.LpTweaks._

import android.support.v4.app.Fragment

import rx._
import rx.ops._


class TaskDetailFragment[T: TreeLike](task: List[T]) extends Fragment with Contexts[Fragment] with RxSupport {
  import TreeLike._


  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    import TextTweaks._
    import CustomTweaks._
    getUi {
      l[LinearLayout](
        w[TextView] <~ text("Title") <~ bold,
        w[TextView] <~ text(task.last.title) <~ selectableText,
        w[TextView] <~ text("Attachment") <~ bold,
        w[TextView] <~ text(task.last.attachment.getOrElse("")) <~ selectableText
      ) <~ vertical
    }
  }
}

object TaskDetailFragment {
  def newInstance[T](task: List[T], treelike: TreeLike[T]) = {
    implicit val tl = treelike
    new TaskDetailFragment[T](task)
  }
}
