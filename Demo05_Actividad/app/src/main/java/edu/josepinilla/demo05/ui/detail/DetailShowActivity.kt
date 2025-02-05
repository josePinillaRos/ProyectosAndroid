package edu.josepinilla.demo05.ui.detail

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import edu.josepinilla.demo05.R
import edu.josepinilla.demo05.RoomApplication
import edu.josepinilla.demo05.adapters.CastAdapter
import edu.josepinilla.demo05.data.RemoteDataSource
import edu.josepinilla.demo05.data.Repository
import edu.josepinilla.demo05.databinding.ActivityDetailShowBinding
import edu.josepinilla.demo05.model.StateShows
import edu.josepinilla.demo05.model.cast.Cast
import edu.josepinilla.demo05.model.shows.ShowsItem
import edu.josepinilla.demo05.utils.checkConnection
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class DetailShowActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailShowBinding

    private val vm: DetailShowViewModel by viewModels {
        val db = (application as RoomApplication).stateShowsDB.stateShowsDao()
        val dataSource = RemoteDataSource(db)
        val repository = Repository(dataSource)
        val showId = intent.getIntExtra(SHOW_ID, -1)
        DetailShowViewModelFactory(repository, showId)
    }

    companion object {
        const val SHOW_ID = "SHOW_ID"

        fun navigate(activity: AppCompatActivity, showId: Int = -1) {
            val intent = Intent(activity, DetailShowActivity::class.java).apply {
                putExtra(SHOW_ID, showId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            activity.startActivity(
                intent,
                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.mToolbar.navigationIcon = AppCompatResources.getDrawable(this, R.drawable.ic_arrow_back)
        binding.mToolbar.setNavigationOnClickListener {
            finishAfterTransition()
        }

        if(checkConnection(this)) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    combine(vm.show, vm.casting, vm.stateShow) { show, cast, state ->
                        // Actualizamos el estado del show en base a la BD
                        show.favorite = state?.stateFavorite ?: false
                        show.watched = state?.stateWatched ?: false

                        // Aquí ya puedes hacer setImageState igual que en el adapter
                        binding.ivFavDetail.setImageState(intArrayOf(R.attr.state_on), show.favorite)
                        binding.ivWatchedDetail.setImageState(intArrayOf(R.attr.state_on), show.watched)

                        binding.mToolbar.title = show.name
                        binding.tvAverageDetail.text = show.rating?.average?.toString()
                        binding.tvProducerDetail.text = show.network?.name
                        binding.tvGenderDetail.text = show.genres?.joinToString(", ")
                        binding.tvStatus.text = show.status
                        binding.tvSummary.text = Html.fromHtml(show.summary, Html.FROM_HTML_MODE_COMPACT)

                        Glide.with(this@DetailShowActivity)
                            .load(show.image?.original)
                            .transform(FitCenter(), RoundedCorners(8))
                            .into(binding.ivShow)

                        val adapter = CastAdapter()
                        binding.recyclerCast.adapter = adapter
                        adapter.submitList(cast.sortedBy { it.person?.name })

                        // Aquí replicamos la lógica del adapter: llamamos siempre a setImageState()
                        // con R.attr.state_on y el valor booleano. Sin condiciones.
                        binding.ivFavDetail.setImageState(intArrayOf(R.attr.state_on), show.favorite)
                        binding.ivWatchedDetail.setImageState(intArrayOf(R.attr.state_on), show.watched)

                        // Listeners: sólo actualizan BD; la interfaz se refrescará en la siguiente emisión
                        binding.ivFavDetail.setOnClickListener {
                            vm.updateStateShows(
                                StateShows(
                                    idShow = show.id!!,
                                    stateFavorite = !show.favorite,
                                    stateWatched = show.watched
                                )
                            )
                        }

                        binding.ivWatchedDetail.setOnClickListener {
                            vm.updateStateShows(
                                StateShows(
                                    idShow = show.id!!,
                                    stateFavorite = show.favorite,
                                    stateWatched = !show.watched
                                )
                            )
                        }
                    }
                        .catch {
                            Toast.makeText(this@DetailShowActivity, it.message, Toast.LENGTH_SHORT).show()
                        }
                        .collect()
                }
            }
        } else {
            Toast.makeText(
                this@DetailShowActivity,
                getString(R.string.txt_noConnection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}