package com.example.storagemanager.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.storagemanager.R;
import com.example.storagemanager.StorageModel;
import com.example.storagemanager.StorageModelDAO;
import com.example.storagemanager.databinding.FragmentStoragesBinding;
import com.example.storagemanager.ui.dashboard.RentAddNew;

import java.util.ArrayList;

public class StorageFragment extends Fragment {

    private FragmentStoragesBinding binding;

    private RecyclerView recyclerView;
    private StoragesAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        StorageViewModel storageViewModel = new ViewModelProvider(this).get(StorageViewModel.class);

        binding = FragmentStoragesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        StorageModelDAO dao = new StorageModelDAO(this.getContext());
        adapter = new StoragesAdapter(dao.getList(),this.getContext());

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Fragment", "Voltou para fragment Storage");
        adapter.update();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)    {
        inflater.inflate(R.menu.storage_menu, menu);
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
                Intent intent = new Intent(getActivity(), StorageAddNew.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

class StoragesAdapter extends RecyclerView.Adapter<StoragesViewHolder> {
    private ArrayList<StorageModel> storages;
    StorageModelDAO dao;

    private Context mCtx;

    public StoragesAdapter(ArrayList<StorageModel> storages, Context mCtx) {
        this.storages = storages;
        this.mCtx = mCtx;
        this.dao = new StorageModelDAO(mCtx);
        update();
    }

    public StoragesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StoragesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.storage_list_item, parent, false));
    }


    public void onBindViewHolder(StoragesViewHolder holder, int position) {
        holder.tipoCategoria.setText(storages.get(holder.getAdapterPosition()).getCategoryString());
        holder.price.setText("R$ "+ Float.toString(storages.get(position).getPrice()));
        holder.nroStorage.setText("Nro "+Integer.toString(storages.get(position).getNro()));
        holder.id = storages.get(position).getId();

        holder.btnMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mCtx, holder.btnMoreMenu);
                popup.inflate(R.menu.storage_item_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        switch (item.getItemId()) {
                            case R.id.newRent:
                                intent = new Intent(mCtx, RentAddNew.class);
                                intent.putExtra("storageId", storages.get(position).getId());
                                mCtx.startActivity(intent);
                                break;
                            case R.id.editStorage:
                                Log.i("Botao", "Botao de editar clicado!");
                                intent = new Intent(mCtx, StorageAddNew.class);
                                intent.putExtra("storageId", storages.get(position).getId());
                                mCtx.startActivity(intent);
                                break;
                            case R.id.deleteStorage:
                                Log.i("Botao", "Botao de remover clicado!");
                                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                                builder.setTitle("Confirmação")
                                        .setMessage("Tem certeza que deseja excluir este Storage?")
                                        .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int index) {
                                                int adapterPos = holder.getAdapterPosition();
                                                StorageModel storage = storages.get(adapterPos);
                                                StorageModelDAO dao = new StorageModelDAO(view.getContext());
                                                boolean sucesso = dao.delete(storage.getId());
                                                if(sucesso) {
                                                    storages.remove(adapterPos);
                                                    notifyItemRemoved(adapterPos);
                                                    Toast.makeText(view.getContext(), "Storage removido!", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    Toast.makeText(view.getContext(), "Storage não pode ser removido!", Toast.LENGTH_LONG).show();
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
    public void update() { storages = dao.getList(); }

    public int getItemCount() { return storages.size(); }
}

class StoragesViewHolder extends RecyclerView.ViewHolder{
    public Context context;
    public TextView tipoCategoria, price, nroStorage;
    public int id;
    public TextView btnMoreMenu;

    public StoragesViewHolder(View view) {
        super(view);
        tipoCategoria = view.findViewById(R.id.tipoCategoria);
        price = view.findViewById(R.id.storagePrice);
        nroStorage = view.findViewById(R.id.rentPeriodo);
        btnMoreMenu = view.findViewById(R.id.btnMoreMenu);

    }
}

