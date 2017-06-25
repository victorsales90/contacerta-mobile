package br.unifor.victor.contacerta.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import br.unifor.victor.contacerta.fragment.ContaFragment;
import br.unifor.victor.contacerta.fragment.ContatosFragment;
import br.unifor.victor.contacerta.fragment.ProdutosFragment;

/**
 * Created by victo on 08/06/2017.
 */

public class TabAdapter extends FragmentStatePagerAdapter {
    private String[] titutolsAbas = {"PRODUTOS" , "PESSOAS" , "CONTA"};


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ProdutosFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
            case 2:
                fragment = new ContaFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return titutolsAbas.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titutolsAbas[position];
    }
}
