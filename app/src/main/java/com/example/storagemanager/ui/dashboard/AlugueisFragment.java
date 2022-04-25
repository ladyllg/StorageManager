package com.example.storagemanager.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storagemanager.AluguelModel;
import com.example.storagemanager.AluguelModelDAO;
import com.example.storagemanager.ClienteModel;
import com.example.storagemanager.ClienteModelDAO;
import com.example.storagemanager.DbGateway;
import com.example.storagemanager.R;
import com.example.storagemanager.databinding.FragmentAlugueisBinding;
import com.example.storagemanager.ui.notifications.ClientAddNew;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlugueisFragment extends Fragment {

    private FragmentAlugueisBinding binding;
    private RecyclerView recyclerView;
    private RentsAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        AlugueisViewModel alugueisViewModel = new ViewModelProvider(this).get(AlugueisViewModel.class);

        setHasOptionsMenu(true);
        binding = FragmentAlugueisBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        AluguelModelDAO dao = new AluguelModelDAO(this.getContext());
        adapter = new RentsAdapter(dao.getList(), this.getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Fragment", "Voltou para fragment Alugueis");
        adapter.update();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)    {
        inflater.inflate(R.menu.alugueis_menu, menu);
        return;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                AlertDialog.Builder alert = new AlertDialog.Builder(this.getActivity());
                alert.setMessage("Self Storage Manager v2.0")
                        .setNeutralButton("Ok", null)
                        .show();
                return true;
            case R.id.new_item:
                Intent intent = new Intent(getActivity(), RentAddNew.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

class RentsAdapter extends RecyclerView.Adapter<RentsViewHolder> {
    private ArrayList<AluguelModel> rents;
    private DbGateway gw;
    AluguelModelDAO dao;
    private Context mCtx;

    public RentsAdapter(ArrayList<AluguelModel> rents, Context context) {
        this.rents = rents;
        this.mCtx = context;
        this.dao = new AluguelModelDAO(context);
        gw = DbGateway.getInstance(context);
        update();
    }

    public  void update(){
        rents = dao.getList();
    }

    public RentsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RentsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rent_list_item, parent, false));
    }

    public void removerRent(AluguelModel rent){
        int position = rents.indexOf(rent);
        rents.remove(position);
        notifyItemRemoved(position);
    }

    public void onBindViewHolder(RentsViewHolder holder, int position) {
        // Buscando pelo storage no banco de dados
        Cursor cursor = gw.getDatabase().rawQuery("SELECT categoria, nro FROM storages WHERE ID = " + rents.get(position).getStorageId(), null);
        if(cursor != null && cursor.moveToFirst()){
            String infoStorage = cursor.getString(0) + " #" +cursor.getString(1);
            holder.tipoStorage.setText(infoStorage);
        }

        // Buscando pelo cliente no banco de dados
        cursor = gw.getDatabase().rawQuery("SELECT nome FROM clients WHERE ID = " + rents.get(position).getClientId(), null);
        if(cursor != null && cursor.moveToFirst()){
            holder.nomeCliente.setText("Alugado por "+ cursor.getString(0));
        }

        holder.rentPeriodo.setText("De " + rents.get(position).getStartDateTempl() + " até " + rents.get(position).getEndDateTempl());
        holder.id = rents.get(position).getId();

        holder.btnRentMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mCtx, holder.btnRentMoreMenu);
                popup.inflate(R.menu.aluguel_item_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.editRent:
                                intent = new Intent(mCtx, RentAddNew.class);
                                intent.putExtra("rentId", rents.get(position).getId());
                                intent.putExtra("storageId", rents.get(position).getStorageId());
                                intent.putExtra("clientId", rents.get(position).getClientId());
                                Log.i("Novo Aluguel", "opcao selecionada");
                                mCtx.startActivity(intent);
                                break;
                            case R.id.renewRent:
                                intent = new Intent(mCtx, EditRent.class);
                                intent.putExtra("rentId", rents.get(position).getId());
                                mCtx.startActivity(intent);
                                break;
                            case R.id.deleteRent:
                                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                                builder.setTitle("Confirmação")
                                        .setMessage("Tem certeza que deseja excluir este Aluguel?")
                                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int index) {
                                                int adapterPos = holder.getAdapterPosition();
                                                AluguelModel rent = rents.get(adapterPos);
                                                AluguelModelDAO dao = new AluguelModelDAO(view.getContext());
                                                boolean sucesso = dao.delete(rent.getId());
                                                if(sucesso) {
                                                    removerRent(rent);
                                                    Toast.makeText(view.getContext(), "Aluguel removido!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(view.getContext(), "Erro ao excluir aluguel!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancelar", null)
                                        .create()
                                        .show();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }

    public int getItemCount() { return rents.size(); }
}

class RentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public Context context;
    public TextView tipoStorage, nomeCliente, rentPeriodo;
    public int id;
    public TextView btnRentMoreMenu;

    public RentsViewHolder(View view) {
        super(view);
        tipoStorage = view.findViewById(R.id.tipoCategoria);
        nomeCliente = view.findViewById(R.id.storageClient);
        rentPeriodo = view.findViewById(R.id.rentPeriodo);
        btnRentMoreMenu = view.findViewById(R.id.btnRentMoreMenu);
        view.setOnClickListener(this);
    }

    public void onClick(View v) {

    }
}