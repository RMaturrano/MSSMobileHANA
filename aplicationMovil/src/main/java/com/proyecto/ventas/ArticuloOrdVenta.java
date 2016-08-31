package com.proyecto.ventas;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.proyect.movil.R;
import com.proyecto.bean.AlmacenBean;
import com.proyecto.bean.ArticuloBean;
import com.proyecto.bean.GrupoUnidadMedidaBean;
import com.proyecto.bean.ImpuestoBean;
import com.proyecto.bean.ListaPrecioBean;
import com.proyecto.bean.UnidadMedidaBean;
import com.proyecto.database.Select;
import com.proyecto.utils.DoubleRound;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;
import com.proyecto.utils.Variables;

import java.util.ArrayList;

public class ArticuloOrdVenta extends Fragment {

    private View v = null;
    private Context contexto;
    private int iconId = Variables.idIconRightBlue36dp;
    private AlmacenBean almacenSel = null;
    private ImpuestoBean listaImpuestoSel = null;
    private UnidadMedidaBean unidadMedidaSel = null;
    private GrupoUnidadMedidaBean grupoUnidadMedidaSel = null;
    private ArticuloBean art_bean = null;
    private ListaPrecioBean listaPreSel = null;

    private SharedPreferences pref;
    private String permisoEscogerPrecio = "";

    //Listas de carga
    private ArrayList<AlmacenBean> listaAlmacen;
    private ArrayList<ImpuestoBean> listaImpuesto;
    private static ArrayList<UnidadMedidaBean> listaUnidadesMedida;
    private static ArrayList<UnidadMedidaBean> listaUnidadesMedidaManual;
    private ArrayList<ListaPrecioBean> listaPrecios = null;

    //LIST VIEW PRINCIPAL QUE CONTIENE A TODO
    private ListView lvPrincipal = null;
    private ArrayList<FormatCustomListView> searchResults = null;
    private ListViewCustomAdapterTwoLinesAndImg adapter;

    private FormatCustomListView fullObject = null;
    private int posicion = 0;
    private double preBruto = 0;
    private double total = 0;
    private double precio = 0;
    private double descuento = 0;
    private double impuesto = 0;
    private double cantidad = 0;

    //Precio venta original
    private String precioVenta;

    //Unidad de medida original
    private UnidadMedidaBean unidadMedidaSelOriginal = null;

    private ArticuloBean articuloActualizar = null;
    private String actualizar = "false";

    //RECIBE LOS PARÀMETROS DESDE EL FRAGMENT CORRESPONDIENTE
    private BroadcastReceiver myLocalBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();

            if (bundle != null) {

                if (intent.getAction().equals("buscar_art")) {

                    String cod = bundle.getString("cod");
                    String descp = bundle.getString("desc");
                    String[] extras = bundle.getString("extras").toString().split("¡");

                    if (!extras[2].equals("") && !extras[2].equalsIgnoreCase("anytype{}") &&
                            !extras[4].equals("") && !extras[4].equalsIgnoreCase("anytype{}")) {
                        grupoUnidadMedidaSel = new GrupoUnidadMedidaBean();
                        grupoUnidadMedidaSel.setNombre(extras[4]);
                        grupoUnidadMedidaSel.setCodigo(extras[2]);

                        fullObject = new FormatCustomListView();
                        fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(2);
                        fullObject.setData(grupoUnidadMedidaSel.getNombre());
                        searchResults.set(2, fullObject);

                        if (!grupoUnidadMedidaSel.getCodigo().equals("-1")) {
                            cargarListasUMManual(grupoUnidadMedidaSel.getCodigo());
                        }

                    }


                    if (!extras[3].equals("") && !extras[3].equalsIgnoreCase("anytype{}")) {
                        String unidadMedidaVentaCodigo = extras[3];

                        Select select = new Select(contexto);
                        listaUnidadesMedida = select.listaUnidadesDeMedida();

                        for (int i = 0; i < listaUnidadesMedida.size(); i++) {

                            UnidadMedidaBean bean = listaUnidadesMedida.get(i);

                            if (bean.getCodigo().trim().equalsIgnoreCase(unidadMedidaVentaCodigo.trim())) {
                                unidadMedidaSelOriginal = bean;
                                unidadMedidaSel = bean;
                                fullObject = new FormatCustomListView();
                                fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(3);
                                if (grupoUnidadMedidaSel.getCodigo().equals("-1"))
                                    fullObject.setIcon(0);
                                else
                                    fullObject.setIcon(iconId);
                                fullObject.setData(unidadMedidaSel.getNombre());
                                searchResults.set(3, fullObject);
                                break;
                            }

                        }

                    }

                    Object o = lvPrincipal.getItemAtPosition(0);
                    fullObject = new FormatCustomListView();
                    fullObject = (FormatCustomListView) o;
                    fullObject.setData(cod);
                    searchResults.set(0, fullObject);

                    Object o2 = lvPrincipal.getItemAtPosition(1);
                    fullObject = new FormatCustomListView();
                    fullObject = (FormatCustomListView) o2;
                    fullObject.setData(descp);
                    searchResults.set(1, fullObject);

                    Object o3 = lvPrincipal.getItemAtPosition(5);
                    fullObject = new FormatCustomListView();
                    fullObject = (FormatCustomListView) o3;
                    fullObject.setData("1");
                    searchResults.set(5, fullObject);

                    searchResults.get(6).setData(OrdenVentaFragment.listaPrecioSel.getNombre());

                    precioVenta = cargarPrecioManual(OrdenVentaFragment.listaPrecioSel.getCodigo(), cod);
                    if (precioVenta.equals("") || precioVenta == null)
                        precioVenta = "0.00";
                    fullObject = new FormatCustomListView();
                    fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(7);
                    fullObject.setData(precioVenta);
                    searchResults.set(7, fullObject);

                    Object o6 = lvPrincipal.getItemAtPosition(8);
                    fullObject = new FormatCustomListView();
                    fullObject = (FormatCustomListView) o6;
                    fullObject.setData("0.00");
                    searchResults.set(8, fullObject);

                    doMaths();
                    lvPrincipal.invalidateViews();


                }
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pedido_cliente_articulo, viewGroup, false);

        getActivity().setTitle("Artículo");

        v = view;
        contexto = view.getContext();

        pref = PreferenceManager
                .getDefaultSharedPreferences(contexto);
        permisoEscogerPrecio = pref.getString(Variables.MOVIL_ESCOGER_PRECIO, "");

        //LLENAR EL LISTADO DE DATOS QUE COMPONEN LA ORDEN DE VENTA
        cargarListas();

        if (!MainVentas.codigoArticulo.equals("")) {
            for (ArticuloBean a : OrdenVentaFragment.listaDetalleArticulos) {
                if (a.getCod().equals(MainVentas.codigoArticulo)) {
                    articuloActualizar = new ArticuloBean();
                    articuloActualizar = a;
                    actualizar = "True";
                    break;
                }
            }
        }

        llenarListPrincipal();

        //Registrar los avisos
        IntentFilter filter = new IntentFilter("buscar_art");
        LocalBroadcastManager
                .getInstance(contexto)
                .registerReceiver(myLocalBroadcastReceiver, filter);

        //PROGRAMAR EL CLICK HACIA EL BLOQUE PRINCIPAL PARA CADA DATO
        lvPrincipal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                construirAlert(position);
            }
        });
        ////


        setHasOptionsMenu(true);

        return view;

    }

    private void cargarListas() {

        listaAlmacen = new ArrayList<AlmacenBean>();
        listaImpuesto = new ArrayList<ImpuestoBean>();
        listaUnidadesMedida = new ArrayList<UnidadMedidaBean>();
        listaPrecios = new ArrayList<ListaPrecioBean>();

        Select select = new Select(contexto);
        listaAlmacen = select.listaAlmacen();
        almacenSel = listaAlmacen.get(0);
        listaImpuesto = select.listaImpuesto();
        listaImpuestoSel = listaImpuesto.get(0);
        listaUnidadesMedida = select.listaUnidadesDeMedida();
        listaPrecios = select.listaPrecios();


    }

    private void cargarListasUMManual(String param) {

        listaUnidadesMedidaManual = new ArrayList<UnidadMedidaBean>();

        Select select = new Select(contexto);
        listaUnidadesMedidaManual = select.listaUnidadesDeMedida(param);

        Log.i("codum", param);

        select.close();

    }

    private String cargarPrecioManual(String listaPrecio, String codigoArticulo) {

        Select select = new Select(contexto);
        String precioVenta = select.selectPrecioArticulo(listaPrecio, codigoArticulo);
        select.close();

        return precioVenta;

    }

    private void llenarListPrincipal() {

        searchResults = new ArrayList<FormatCustomListView>();

        lvPrincipal = (ListView) v.findViewById(R.id.lvArticuloPedido);

        FormatCustomListView sr = new FormatCustomListView();
        sr.setTitulo("Artículo");
        if (articuloActualizar != null) {
            sr.setData(articuloActualizar.getCod());
        } else {
            sr.setIcon(iconId);
        }
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Descripción");
        if (articuloActualizar != null)
            sr.setData(articuloActualizar.getDesc());
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Grupo unidad de medida");
        if (articuloActualizar != null) {
            grupoUnidadMedidaSel = new GrupoUnidadMedidaBean();
            grupoUnidadMedidaSel.setCodigo(articuloActualizar.getGrupoArticulo());
            grupoUnidadMedidaSel.setNombre(articuloActualizar.getNombreGrupoArt());
            sr.setData(articuloActualizar.getNombreGrupoArt());
        }
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Unidad de medida");
        sr.setIcon(iconId);
        if (articuloActualizar != null) {
            unidadMedidaSel = new UnidadMedidaBean();
            unidadMedidaSel.setCodigo(articuloActualizar.getCodUM());
            unidadMedidaSel.setNombre(articuloActualizar.getNombreUnidadMedida());
            sr.setData(articuloActualizar.getNombreUnidadMedida());
        }
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Almacén");
        sr.setIcon(iconId);
        if (articuloActualizar != null) {
            for (AlmacenBean almacen : listaAlmacen) {
                if (almacen.getCodigo().equals(articuloActualizar.getAlmacen())) {
                    almacenSel = new AlmacenBean();
                    almacenSel = almacen;
                    sr.setData(almacenSel.getDescripcion());
                }
            }
        } else
            sr.setData(almacenSel.getDescripcion());
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Cantidad");
        sr.setIcon(iconId);
        if (articuloActualizar != null)
            sr.setData(String.valueOf(articuloActualizar.getCant()));
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Lista de precios");
        if (permisoEscogerPrecio.equalsIgnoreCase("Y"))
            sr.setIcon(iconId);
        sr.setData(OrdenVentaFragment.listaPrecioSel.getNombre());
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Precio unitario");
        if (articuloActualizar != null)
            sr.setData(String.valueOf(articuloActualizar.getPre()));
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Porcentaje descuento");
        if (articuloActualizar != null)
            sr.setData(String.valueOf(articuloActualizar.getDescuento()));
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Impuesto");
        if (articuloActualizar != null) {
            for (ImpuestoBean impuesto : listaImpuesto) {
                if (impuesto.getCodigo().equals(articuloActualizar.getCodigoImpuesto())) {
                    listaImpuestoSel = new ImpuestoBean();
                    listaImpuestoSel = impuesto;
                    sr.setData(listaImpuestoSel.getNombre());
                }
            }
        } else
            sr.setData(listaImpuesto.get(0).getNombre());
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Precio bruto");
        if (articuloActualizar != null)
            sr.setData(String.valueOf(articuloActualizar.getPreBruto()));
        searchResults.add(sr);

        sr = new FormatCustomListView();
        sr.setTitulo("Total");
        if (articuloActualizar != null)
            sr.setData(String.valueOf(articuloActualizar.getTotal()));
        searchResults.add(sr);

        adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto, searchResults);
        lvPrincipal.setAdapter(adapter);

    }

    private void construirAlert(int position) {

        if (position == 0) {

            if (actualizar.equalsIgnoreCase("false")) {
                Fragment fragment = new BuscarArtFragment();

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(this);
                transaction.add(R.id.box, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        } else if (position == 3) {

            if (grupoUnidadMedidaSel != null && !grupoUnidadMedidaSel.getCodigo().equals("-1")) {

                String[] parts = grupoUnidadMedidaSel.getNombre().split(":");

                // Parte UNO de la unidad de medida , ejem: 1-CAJA
                String[] part_1 = parts[0].split("-");
                final String part_1_nom = part_1[1];

                // Parte DOS de la unidad de medida , ejem: 12-BOTELLA
                String[] part_2 = parts[1].split("-");
                final String um2 = part_2[1];
                final int nroDividir = Integer.parseInt(part_2[0]);

                //Unidad Medida
                posicion = position;

                //Capturar el objeto
                Object o = lvPrincipal.getItemAtPosition(position);
                fullObject = new FormatCustomListView();
                fullObject = (FormatCustomListView) o;

                AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
                alert.setTitle("Unidad de medida");

                //Spinner
                final Spinner spnAlmacen = new Spinner(contexto);


                ArrayAdapter<UnidadMedidaBean> adapter = new ArrayAdapter<UnidadMedidaBean>(contexto,
                        android.R.layout.simple_list_item_1,
                        listaUnidadesMedidaManual);
                spnAlmacen.setAdapter(adapter);
                spnAlmacen.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View arg1, int pos, long arg3) {
                        unidadMedidaSel = new UnidadMedidaBean();
                        unidadMedidaSel = (UnidadMedidaBean) parent.getItemAtPosition(pos);

                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> arg0) {


                    }
                });

                alert.setView(spnAlmacen);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        // Do something with value!
                        fullObject.setData(unidadMedidaSel.toString());
                        searchResults.set(posicion, fullObject);

                        if (unidadMedidaSel.toString().equals(um2)) {
                            double pre = Double.parseDouble(searchResults.get(7).getData().toString());
                            if (pre > 0) {
                                fullObject = new FormatCustomListView();
                                fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(7);
                                fullObject.setData(String.valueOf(DoubleRound.round((pre / nroDividir), 6)));
                                searchResults.set(7, fullObject);
                            }
                        } else if (unidadMedidaSel.toString().equals(part_1_nom) && unidadMedidaSelOriginal.getNombre().equals(um2)) {

                            searchResults.get(7).setData(String.valueOf(DoubleRound.round((Double.parseDouble(precioVenta) * nroDividir), 6)));

                        } else {
                            fullObject = new FormatCustomListView();
                            fullObject = (FormatCustomListView) lvPrincipal.getItemAtPosition(7);
                            fullObject.setData(precioVenta);
                            searchResults.set(7, fullObject);
                        }
                        doMaths();

                        lvPrincipal.invalidateViews();

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alert.show();
            }

        } else if (position == 4) {

            posicion = position;

            //Capturar el objeto
            Object o = lvPrincipal.getItemAtPosition(position);
            fullObject = new FormatCustomListView();
            fullObject = (FormatCustomListView) o;

            AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
            alert.setTitle("Almacén");

            //Spinner
            final Spinner spnAlmacen = new Spinner(contexto);


            ArrayAdapter<AlmacenBean> adapter = new ArrayAdapter<AlmacenBean>(contexto,
                    android.R.layout.simple_list_item_1,
                    listaAlmacen);
            spnAlmacen.setAdapter(adapter);
            spnAlmacen.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent,
                                           View arg1, int pos, long arg3) {
                    //MONEDA SELECCIONADA
                    almacenSel = new AlmacenBean();
                    almacenSel = (AlmacenBean) parent.getItemAtPosition(pos);

                }

                @Override
                public void onNothingSelected(
                        AdapterView<?> arg0) {


                }
            });

            alert.setView(spnAlmacen);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    // Do something with value!
                    fullObject.setData(almacenSel.toString());
                    searchResults.set(posicion, fullObject);
                    lvPrincipal.invalidateViews();

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();

        } else if (position == 5) {

            posicion = position;
            //Capturar el objeto (row - fila)
            Object o = lvPrincipal.getItemAtPosition(position);
            fullObject = new FormatCustomListView();
            fullObject = (FormatCustomListView) o;

            //Spinner
            final EditText edtCantidad = new EditText(contexto);

            AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
            alert.setTitle("Cantidad");
            edtCantidad.setFocusableInTouchMode(true);
            edtCantidad.requestFocus();
            edtCantidad.setRawInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
            edtCantidad.setMaxLines(1);
            alert.setView(edtCantidad);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtCantidad.getWindowToken(), 0);

                    fullObject.setData(edtCantidad.getText().toString());
                    searchResults.set(posicion, fullObject);
                    lvPrincipal.invalidateViews();

                    doMaths();


                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtCantidad.getWindowToken(), 0);
                }
            });

            edtCantidad.requestFocus();
            InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            alert.show();


        } else if (position == 6) {

            if (permisoEscogerPrecio.equalsIgnoreCase("Y") && !searchResults.get(0).toString().equals("")) {
                //LISTA DE PRECIOS
                posicion = position;
                // Capturar el objeto
                Object o = lvPrincipal.getItemAtPosition(position);
                fullObject = new FormatCustomListView();
                fullObject = (FormatCustomListView) o;
                //

                // Spinner
                final Spinner spn = new Spinner(contexto);

                ArrayAdapter<ListaPrecioBean> adap = new ArrayAdapter<ListaPrecioBean>(
                        contexto, android.R.layout.simple_list_item_1, listaPrecios);

                spn.setAdapter(adap);

                spn.setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View arg1,
                                               int pos, long arg3) {

                        listaPreSel = new ListaPrecioBean();
                        listaPreSel = (ListaPrecioBean) parent
                                .getItemAtPosition(pos);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                });

                AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
                alert.setTitle("Lista de precios");

                // Set an EditText view to get user input
                alert.setView(spn);

                alert.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                                fullObject.setData(listaPreSel
                                        .getNombre());
                                searchResults.set(posicion, fullObject);

                                precioVenta = cargarPrecioManual(listaPreSel.getCodigo(), searchResults.get(0).getData());
                                if (precioVenta.equals("") || precioVenta == null)
                                    precioVenta = "0.00";
                                searchResults.get(7).setData(precioVenta);
                                searchResults.get(3).setData(unidadMedidaSelOriginal.getNombre());
                                doMaths();
                                lvPrincipal.invalidateViews();
                            }
                        });

                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // Canceled.
                            }
                        });

                alert.show();
            }

        } else if (position == 7) {


        } else if (position == 8) {
            //PORCENTAJE DE DESCUENTO
//			
//			posicion = position;
//			//Capturar el objeto (row - fila) 
//			Object o = lvPrincipal.getItemAtPosition(position);
//			fullObject = new FormatCustomListView();
//	    	fullObject = (FormatCustomListView)o;
//
//			//Spinner
//			final EditText edtDescuento = new EditText(contexto);
//			
//			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
//			alert.setTitle("Porcentaje de descuento");
//			alert.setView(edtDescuento);
//			edtDescuento.setFocusableInTouchMode(true);
//			edtDescuento.requestFocus();
//			
//			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int whichButton) {
//			
//				InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
//	            imm.hideSoftInputFromWindow(edtDescuento.getWindowToken(), 0);
//				
//				fullObject.setData(edtDescuento.getText().toString());
//				searchResults.set(posicion, fullObject);
//				lvPrincipal.invalidateViews();
//
//				doMaths();
//				
//				
//			  }
//			});
//
//			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//			  public void onClick(DialogInterface dialog, int whichButton) {
//			    // Canceled.
//			  }
//			});
//
//			edtDescuento.requestFocus();
//	        InputMethodManager imm = (InputMethodManager) contexto.getSystemService(Context.INPUT_METHOD_SERVICE);
//	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//			alert.show();

        } else if (position == 9) {
            //IMPUESTO

//			posicion = position;
//			
//			//Capturar el objeto
//			Object o = lvPrincipal.getItemAtPosition(position);
//    		fullObject = new FormatCustomListView();
//        	fullObject = (FormatCustomListView)o;
//			
//			AlertDialog.Builder alert = new AlertDialog.Builder(contexto);
//    		alert.setTitle("Impuesto");
//
//    		//Spinner
//    		final Spinner spn = new Spinner(contexto);
//    		
//    		
//    		ArrayAdapter<ImpuestoBean> adapter = new ArrayAdapter<ImpuestoBean>(contexto, 
//					android.R.layout.simple_list_item_1,
//					listaImpuesto);
//    		spn.setAdapter(adapter);
//    		spn.setOnItemSelectedListener(new OnItemSelectedListener() {
//    			
//				@Override
//				public void onItemSelected(AdapterView<?> parent,
//						View arg1, int pos, long arg3) {
//					//MONEDA SELECCIONADA
//					listaImpuestoSel = new ImpuestoBean();
//					listaImpuestoSel = (ImpuestoBean) parent.getItemAtPosition(pos);
//					
//				}
//
//				@Override
//				public void onNothingSelected(
//						AdapterView<?> arg0) {
//					
//					
//				}
//			});
//    		
//    		alert.setView(spn);
//    		
//    		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//    		public void onClick(DialogInterface dialog, int whichButton) {
//
//    		  // Do something with value!
//    			fullObject.setData(listaImpuestoSel.toString());
//				searchResults.set(posicion, fullObject);
//				doMaths();
//				lvPrincipal.invalidateViews();
//				
//    		  }
//    		});
//
//    		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//    		  public void onClick(DialogInterface dialog, int whichButton) {
//    		    // Canceled.
//    		  }
//    		});
//
//    		alert.show();


        }
    }


    private void doMaths() {

        preBruto = 0;
        total = 0;
        precio = 0;
        descuento = 0;
        impuesto = 0;
        cantidad = 0;

        Object oPre = lvPrincipal.getItemAtPosition(7);
        fullObject = new FormatCustomListView();
        fullObject = (FormatCustomListView) oPre;
        if (fullObject.getData() != null && !fullObject.getData().equals("")) {
            precio = Double.parseDouble(fullObject.getData());
        }

        Object oCan = lvPrincipal.getItemAtPosition(5);
        fullObject = new FormatCustomListView();
        fullObject = (FormatCustomListView) oCan;
        if (fullObject.getData() != null && !fullObject.getData().trim().equals("")) {
            if(!fullObject.getData().contains(" ") && !fullObject.getData().startsWith(".") &&
                    !fullObject.getData().startsWith(",") && !fullObject.getData().startsWith("-") &&
                    !fullObject.getData().contains("-"))
            cantidad = Double.parseDouble(fullObject.getData());
        }

        Object oDes = lvPrincipal.getItemAtPosition(8);
        fullObject = new FormatCustomListView();
        fullObject = (FormatCustomListView) oDes;
        if (fullObject.getData() != null && !fullObject.getData().equals("")) {
            descuento = (Double.parseDouble(fullObject.getData())) / 100;
        }

        if (!listaImpuestoSel.getCodigo().equals("IGV_EXO")) {
            impuesto = 0.18;
        }

        double totalDescuento = precio * descuento;
        double precioMenosDescuento = precio - totalDescuento;
        double totalImpuesto = precioMenosDescuento * impuesto;

        preBruto = DoubleRound.round(precioMenosDescuento + totalImpuesto, 6);
        Object oPreBruto = lvPrincipal.getItemAtPosition(10);
        fullObject = new FormatCustomListView();
        fullObject = (FormatCustomListView) oPreBruto;
        fullObject.setData(String.valueOf(preBruto));
        searchResults.set(10, fullObject);

        total = DoubleRound.round(((precio * cantidad) - (precio * cantidad) * descuento), 6);
        Object oTotal = lvPrincipal.getItemAtPosition(11);
        fullObject = new FormatCustomListView();
        fullObject = (FormatCustomListView) oTotal;
        fullObject.setData(String.valueOf(total));
        searchResults.set(11, fullObject);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_art_ordventa, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        switch (item.getItemId()) {

            case 16908332:

                if (!MainVentas.codigoArticulo.equals(""))
                    MainVentas.codigoArticulo = "";

                transaction.remove(this);
                transaction.commit();

                getActivity().setTitle("Contenido");
                getFragmentManager().popBackStack();

                return true;

            case R.id.action_aceptar:

                if (searchResults.get(0).getData() != "" && searchResults.get(0).getData() != null &&
                        searchResults.get(5).getData() != "" && searchResults.get(5).getData() != null) {

                    Bundle arguments = new Bundle();
                    String cabe = searchResults.get(0).getData() + " - " + searchResults.get(1).getData();
                    String desc = searchResults.get(4).getData();

                    arguments.putString("cabe", cabe);
                    arguments.putString("desc", desc);


                    art_bean = new ArticuloBean();
                    art_bean.setCod(searchResults.get(0).getData());
                    art_bean.setDesc(searchResults.get(1).getData());
                    art_bean.setGrupoArticulo(grupoUnidadMedidaSel.getCodigo());
                    art_bean.setNombreGrupoArt(grupoUnidadMedidaSel.getNombre());
                    art_bean.setCodUM(unidadMedidaSel.getCodigo());
                    art_bean.setNombreUnidadMedida(unidadMedidaSel.getNombre());
                    art_bean.setAlmacen(almacenSel.getCodigo());
                    art_bean.setCant(Double.parseDouble(searchResults.get(5).getData()));
                    art_bean.setPre(Double.parseDouble(searchResults.get(7).getData()));
                    art_bean.setDescuento(Double.parseDouble(searchResults.get(8).getData()));
                    if (!listaImpuestoSel.getCodigo().equals("IGV_EXO"))
                        art_bean.setImpuesto(18);
                    else
                        art_bean.setImpuesto(0);
                    art_bean.setCodigoImpuesto(listaImpuestoSel.getCodigo());
                    art_bean.setUtilIcon(iconId);


                    if (MainVentas.codigoArticulo.equals(""))
                        OrdenVentaFragment.listaDetalleArticulos.add(art_bean);
                    else {
                        for (int i = 0; i < OrdenVentaFragment.listaDetalleArticulos.size(); i++) {
                            if (OrdenVentaFragment.
                                    listaDetalleArticulos.
                                    get(i).
                                    getCod().equals(art_bean.getCod())) {
                                OrdenVentaFragment.listaDetalleArticulos.remove(i);
                                OrdenVentaFragment.listaDetalleArticulos.add(i, art_bean);
                                break;
                            } else
                                i++;
                        }
                        MainVentas.codigoArticulo = "";
                    }


                    //MANDAR LOS PARÀMETROS EN LOCALBORADCAST INTENT
                    Intent localBroadcastIntent = new Intent("event-send-art-to-list");
                    localBroadcastIntent.putExtras(arguments);
                    LocalBroadcastManager myLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
                    myLocalBroadcastManager.sendBroadcast(localBroadcastIntent);

                    transaction.remove(this);
                    transaction.commit();

                    getActivity().setTitle("Contenido");
                    getActivity().getFragmentManager().popBackStack();

                } else {

                    Toast.makeText(contexto, "Seleccione el artículo o ingrese el precio correspondiente", Toast.LENGTH_LONG).show();

                }


                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
