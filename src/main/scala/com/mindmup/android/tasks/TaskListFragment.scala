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

import rx._
import rx.ops._


class TaskListFragment(currentTasks: Rx[Seq[List[Map[String, Any]]]], taskFilterString: Rx[String]) extends Fragment with Contexts[Fragment] with RxSupport {
  import FilterableListableListAdapter._
  import Implicits._

  lazy val taskListView = w[ListView] <~
    currentTasks.map(t => taskListable.filterableListAdapterTweak(t, MindmupModel.queryInterpreter)) <~
    taskFilterString.map { fs =>
      Tweak[ListView] { lv =>
        val adapter = lv.getAdapter.asInstanceOf[ListableListAdapter[_, _]]
        if(adapter != null) {
          adapter.getFilter.filter(fs)
        }
      }
    }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    getUi { taskListView }
  }
}

object TaskListFragment {
  def newInstance(currentTasks: Rx[Seq[List[Map[String, Any]]]], taskFilterString: Rx[String]) = new TaskListFragment(currentTasks, taskFilterString: Rx[String])
}
