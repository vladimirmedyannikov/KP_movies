package nik.borisov.kpmovies.presentation.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import nik.borisov.kpmovies.R
import nik.borisov.kpmovies.databinding.FragmentMovieDetailBinding
import nik.borisov.kpmovies.domain.entities.Movie
import nik.borisov.kpmovies.presentation.detail.adapters.ReviewsAdapter
import nik.borisov.kpmovies.presentation.detail.adapters.TrailersAdapter

class MovieDetailFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MovieDetailViewModel::class.java]
    }

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding: FragmentMovieDetailBinding
        get() = _binding ?: throw NullPointerException("FragmentMovieDetailBinding is null")

    private val trailersAdapter = TrailersAdapter()
    private val reviewsAdapter = ReviewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var movie: Movie

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        setupClickListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() {
        val trailersRecyclerView = binding.rvTrailers
        trailersRecyclerView.adapter = trailersAdapter
        trailersRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        val reviewsRecyclerView = binding.rvReviews
        reviewsRecyclerView.adapter = reviewsAdapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun observeViewModel() {
        viewModel.getMovie(getMovieId())
        viewModel.movie.observe(viewLifecycleOwner) {
            setupView(it)
            movie = it
            trailersAdapter.submitList(it.trailers)
            reviewsAdapter.submitList(it.reviews)
        }
    }

    private fun setupView(movie: Movie) {
        Glide.with(this)
            .load(movie.poster)
            .into(binding.ivPoster)
        binding.tvMovieName.text = movie.name
        binding.tvMovieRating.text = "Rating KP: %.1f".format(movie.rating)
        binding.tvMovieYearAndGenre.text = "%d, %s".format(
            movie.year,
            movie.genres.toString().trim('[', ']')
        )
        binding.tvMovieCountryAndLength.text = "%s, %d:%02d".format(
            movie.countries.toString().trim('[', ']'),
            movie.movieLength / MIN_IN_HOUR,
            movie.movieLength % MIN_IN_HOUR
        )
        binding.tvMovieDescription.text = movie.description
    }

    private fun setupClickListeners() {
        trailersAdapter.onTrailerClickListener = {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            startActivity(intent)
        }
        reviewsAdapter.onReviewClickListener = {
            val instance = ReviewFragment.newInstance(it)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.movieDetailContainer, instance)
                .addToBackStack(null)
                .commit()
        }
        binding.tvReviewTitle.setOnClickListener {
            val instance = ReviewListFragment.newInstance(movie.name, movie.id)
            requireActivity().supportFragmentManager.beginTransaction()
                .add(R.id.movieDetailContainer, instance)
                .addToBackStack(null)
                .commit()


        }
    }

    private fun getMovieId(): Int {
        return requireArguments().getInt(ARG_MOVIE_ID)
    }

    companion object {

        private const val MIN_IN_HOUR = 60
        private const val ARG_MOVIE_ID = "movie_id"

        fun newInstance(movieId: Int) = MovieDetailFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MOVIE_ID, movieId)
            }
        }
    }
}