package id.com.wanderwisely.ui.survey

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.com.wanderwisely.R
import id.com.wanderwisely.databinding.ActivitySurveyBinding
import id.com.wanderwisely.ui.home.HomeActivity

class SurveyActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySurveyBinding
    private lateinit var surveyViewModel: SurveyViewModel
    private val selectedHobbies = mutableListOf<String>()
    private val selectedTypes = mutableListOf<String>()
    private var budgetMin: Int = 0
    private var budgetMax: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val surveyViewModelFactory = SurveyViewModelFactory(this@SurveyActivity.application)
        surveyViewModel =
            ViewModelProvider(this, surveyViewModelFactory)[SurveyViewModel::class.java]
        binding.btnContinue.setOnClickListener {
            surveyViewModel.submitSurveyData(selectedHobbies, selectedTypes, budgetMin, budgetMax)
        }
        surveyViewModel.surveyData.observe(this) { data ->
            if (data != null && selectedHobbies.isNotEmpty() && selectedTypes.isNotEmpty() && (budgetMin != 0 || budgetMax != 0)) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@SurveyActivity,
                    "Please fill the survey completely",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {
            val checked = view.isChecked
            val selectedValue = when (view.id) {
                R.id.Hiking -> "mendaki"
                R.id.Snorkeling -> "menyelam"
                R.id.Photography -> "berfoto"
                R.id.Trekking -> "trekking"
                R.id.Outbound -> "outbound"
                R.id.Culinary -> "kuliner"
                R.id.Popular -> "populer"
                R.id.Quiet -> "tenang"
                R.id.Lodge -> "penginapan"
                R.id.Camp -> "berkemah"
                R.id.Family -> "keluarga"
                R.id.Alone -> "sendiri"
                R.id.National_Park -> "taman nasional"
                R.id.Beach -> "pantai"
                R.id.Nature -> "alam"
                R.id.History -> "sejarah"
                else -> ""
            }

            when (view.id) {
                R.id.Hiking, R.id.Snorkeling, R.id.Photography, R.id.Trekking, R.id.Outbound, R.id.Culinary -> {
                    if (checked) selectedHobbies.add(selectedValue)
                }
                R.id.Popular, R.id.Quiet, R.id.Lodge, R.id.Camp, R.id.Family, R.id.Alone, R.id.National_Park, R.id.Beach, R.id.Nature, R.id.History -> {
                    if (checked) selectedTypes.add(selectedValue)
                }
            }
        }
    }
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val isChecked = view.isChecked

            if (isChecked) {
                when (view.id) {
                    R.id.type1 -> {
                        binding.type2.isChecked = false
                        binding.type3.isChecked = false
                        binding.type4.isChecked = false
                        budgetMin = 0
                        budgetMax = 50000
                    }
                    R.id.type2 -> {
                        binding.type1.isChecked = false
                        binding.type3.isChecked = false
                        binding.type4.isChecked = false
                        budgetMin = 50000
                        budgetMax = 100000
                    }
                    R.id.type3 -> {
                        binding.type1.isChecked = false
                        binding.type2.isChecked = false
                        binding.type4.isChecked = false
                        budgetMin = 100000
                        budgetMax = 250000
                    }
                    R.id.type4 -> {
                        binding.type1.isChecked = false
                        binding.type2.isChecked = false
                        binding.type3.isChecked = false
                        budgetMin = 250000
                        budgetMax = Int.MAX_VALUE
                    }
                }
            }
        }
    }
}