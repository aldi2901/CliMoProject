package com.climo.al.climoproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.climo.al.climoproject.Models.Postingan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PostingSaya.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PostingSaya#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostingSaya extends Fragment {
    View view;
    private RecyclerView blogListView;
    private List<Postingan> blogList;
    private FirebaseFirestore firebaseFirestore;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton fab;
    private DatabaseReference database;
    private FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private DocumentReference docs;
    private FieldPath field;
    private CollectionReference colls;
    private FieldValue fieldValue;

    public PostingSaya() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostingSaya.
     */
    // TODO: Rename and change types and number of parameters
    public static PostingSaya newInstance(String param1, String param2) {
        PostingSaya fragment = new PostingSaya();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_posting_saya, container, false);
        blogListView = (RecyclerView) view.findViewById(R.id.recyclerView_id);
        blogList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter(blogList, getContext());
        blogListView.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        blogListView.setAdapter(recyclerViewAdapter);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);


        field = FieldPath.of("userID");
        FirebaseUser current = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        //MyRecyclerView();

        GetData();
        /*firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Posts").document(current.getUid()).collection("Detail").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        Postingan posting = doc.getDocument().toObject(Postingan.class);
                        blogList.add(posting);

                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                }

            }
        });*/

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(), PostAjakan.class);
                getActivity().startActivity(intent);
            }
        });

        return view;

    }

    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){
        Toast.makeText(getActivity(),"Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show();
        //Mendapatkan Referensi Database
        database = FirebaseDatabase.getInstance().getReference();
        //DatabaseReference database2 = FirebaseDatabase.getInstance().getReference().child("postingan").child("User").child(String.valueOf(getId()));
        FirebaseAuth current = FirebaseAuth.getInstance();

        database.child("postingansaya").child(current.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        blogList = new ArrayList<>();


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            Postingan postingan = snapshot.getValue(Postingan.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            //mahasiswa.setKey(snapshot.getKey());
                            Log.d("The name", snapshot.getKey());
                            blogList.add(postingan);
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        recyclerViewAdapter = new RecyclerViewAdapter(blogList, getContext());

                        //Memasang Adapter pada RecyclerView
                        blogListView.setAdapter(recyclerViewAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
              /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
                        Toast.makeText(getActivity().getBaseContext(),"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}