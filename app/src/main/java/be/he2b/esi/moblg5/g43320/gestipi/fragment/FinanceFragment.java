package be.he2b.esi.moblg5.g43320.gestipi.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.he2b.esi.moblg5.g43320.gestipi.MainActivity;
import be.he2b.esi.moblg5.g43320.gestipi.R;
import be.he2b.esi.moblg5.g43320.gestipi.databinding.FinanceFragmentBinding;
import be.he2b.esi.moblg5.g43320.gestipi.viewmodel.FinanceViewModel;

/**
 * Represent the sceen where the users can see all the information related to the budget.
 */
public class FinanceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FinanceFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.finance_fragment, container, false);
        View view = binding.getRoot();
        FinanceViewModel viewModel = new FinanceViewModel((MainActivity) getActivity(), ((MainActivity) getActivity()).getCurrentUser());
        binding.setViewModel(viewModel);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
