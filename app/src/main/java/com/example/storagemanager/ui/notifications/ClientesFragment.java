package com.example.storagemanager.ui.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.example.storagemanager.ClienteModel;
import com.example.storagemanager.ClienteModelDAO;
import com.example.storagemanager.R;
import com.example.storagemanager.databinding.FragmentClientesBinding;
import com.example.storagemanager.ui.dashboard.RentAddNew;
import com.example.storagemanager.ui.home.StorageAddNew;

import java.util.ArrayList;

public class ClientesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClientsAdapter adapter;
    private FragmentClientesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        ClientesViewModel clientesViewModel = new ViewModelProvider(this).get(ClientesViewModel.class);
        setHasOptionsMenu(true);
        
        binding = FragmentClientesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        ClienteModelDAO dao = new ClienteModelDAO(this.getContext());
        adapter = new ClientsAdapter(dao.getList("WHERE closed IN (0,1)"),this.getContext());

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
        Log.i("Fragment", "Voltou para fragment Clientes");
        adapter.update();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)    {
        inflater.inflate(R.menu.clientes_menu, menu);
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
                Intent intent = new Intent(getActivity(), ClientAddNew.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

class ClientsAdapter extends RecyclerView.Adapter<ClientsViewHolder> {
    private ArrayList<ClienteModel> clients;
    private Context mCtx;
    ClienteModelDAO dao;

    public ClientsAdapter(ArrayList<ClienteModel> clients, Context mCtx) {
        this.clients = clients;
        this.mCtx = mCtx;
        this.dao = new ClienteModelDAO(mCtx);
        update();
    }

    public void update(){ clients = dao.getList("WHERE closed IN (0,1)");}

    public ClientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ClientsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_list_item, parent, false));
    }

    public void removerCliente(ClienteModel client){
        int position = clients.indexOf(client);
        clients.remove(position);
        notifyItemRemoved(position);
    }

    public void onBindViewHolder(ClientsViewHolder holder, int position) {
        holder.nome.setText(clients.get(position).getName());
        holder.id = clients.get(position).getId();
        holder.idClient.setText(Integer.toString(clients.get(position).getId()));

        Log.i("CLIENTE", ""+clients.get(position).getName()+" STATUS: "+clients.get(position).isClosed());
        if(clients.get(position).isClosed() == 1){
            holder.clienteStatus.setText("FECHADO");
            holder.clienteStatus.setTextColor(Color.parseColor("#BF0010"));
            holder.totalAccount.setText("R$ "+clients.get(position).getTotal());
        }else{
            holder.labelTotal.setVisibility(View.GONE);
            holder.totalAccount.setVisibility(View.GONE);
            holder.clienteStatus.setText("ABERTO");
        }

        holder.btnClientMoreMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mCtx, holder.btnClientMoreMenu);
                popup.inflate(R.menu.client_item_menu);
                if(clients.get(position).isClosed() == 0){
                    popup.getMenu().add(Menu.NONE, 1, 1, "Registrar Aluguel");
                    popup.getMenu().add(Menu.NONE, 2, 2, "Fechar Conta");
                    popup.getMenu().add(Menu.NONE, 3, 3, "Editar");
                    popup.getMenu().add(Menu.NONE, 4, 4, "Fechar Conta");
                }
                popup.getMenu().add(Menu.NONE, 5, 5, "Remover");
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent;
                        int option = item.getItemId();
                        if(item.getItemId() == 2) {
                            intent = new Intent(mCtx, ClientCloseAccount.class);
                            intent.putExtra("clientId", clients.get(position).getId());
                            mCtx.startActivity(intent);
                        }
                        if(item.getItemId() == 1){
                            intent = new Intent(mCtx, RentAddNew.class);
                            intent.putExtra("clientId", clients.get(position).getId());
                            mCtx.startActivity(intent);
                        }
                        if(item.getItemId() == 3) {
                            Log.i("Botao", "Botao de editar clicado!");
                            intent = new Intent(mCtx, ClientAddNew.class);
                            intent.putExtra("clientId", clients.get(position).getId());
                            mCtx.startActivity(intent);
                        }
                        if(item.getItemId() == 5) {
                            Log.i("Botao", "Botao de remover clicado!");
                            AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                            builder.setTitle("Confirmação")
                                    .setMessage("Tem certeza que deseja excluir este cliente?")
                                    .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int index) {
                                            int adapterPos = holder.getAdapterPosition();
                                            ClienteModel client = clients.get(adapterPos);
                                            ClienteModelDAO dao = new ClienteModelDAO(view.getContext());
                                            boolean sucesso = dao.delete(client.getId());
                                            if (sucesso) {
                                                removerCliente(client);
                                                Toast.makeText(view.getContext(), "Cliente removido!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(view.getContext(), "Erro ao excluir storage!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .create()
                                    .show();
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    public int getItemCount() { return clients.size(); }

}

    class ClientsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Context context;
        public TextView nome, idClient, clienteStatus, totalAccount, labelTotal;
        public int id;
        public TextView btnClientMoreMenu;

        public ClientsViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.nomeCliente);
            idClient = view.findViewById(R.id.idCliente);
            btnClientMoreMenu = view.findViewById(R.id.btnClientMoreMenu);
            clienteStatus = view.findViewById(R.id.inputStatus);
            totalAccount = view.findViewById(R.id.totalConta);
            labelTotal = view.findViewById(R.id.labelTotal);
        }

        public void onClick(View v) {
            Toast.makeText(context, "Usuário " + this.nome.getText().toString(), Toast.LENGTH_LONG)
                    .show();
        }
}