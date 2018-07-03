package com.proyecto.sociosnegocio;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.proyect.movil.R;
import com.proyecto.database.DataBaseHelper;
import com.proyecto.utils.Constantes;
import com.proyecto.utils.DynamicHeight;
import com.proyecto.utils.FormatCustomListView;
import com.proyecto.utils.ListViewCustomAdapterTwoLinesAndImg;

public class DetalleSocioNegocioTabPrin extends Fragment {

	private View v;
	private ListView lvInfoBasica = null;
	private ArrayList<FormatCustomListView> searchResults = null;
	private Context contexto;
	private String idBP = "";
	private ListViewCustomAdapterTwoLinesAndImg adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.socio_negocio_detalle_fragment_tab1,
				container, false);

		contexto = v.getContext();
		if (DetalleSocioNegocioMain.idBusinessPartner != null) {

			idBP = DetalleSocioNegocioMain.idBusinessPartner;
			getItemsOfBusinessPartner();

		}

		setHasOptionsMenu(true);
		return v;

	}

	private void getItemsOfBusinessPartner() {

		searchResults = new ArrayList<FormatCustomListView>();

		lvInfoBasica = (ListView) v.findViewById(R.id.lvEditarSNTab1);

		// TRAER TODO DE SQLITE
		DataBaseHelper helper = DataBaseHelper.getHelper(contexto);
		SQLiteDatabase db = helper.getDataBase();
		
//		MyDataBase cn = new MyDataBase(contexto, null, null,r
//				MyDataBase.DATABASE_VERSION);
//		SQLiteDatabase db = cn.getWritableDatabase();

		Cursor rs = db.rawQuery(
				"select  TipoPersona, TP.DES_TIP, TD.DES_DOC, NumeroDocumento, "
						+ "NombreComercial, ApellidoPaterno, ApellidoMaterno, " 
						+ "PrimerNombre, SegundoNombre, "
						+ "G.NOMBRE, "
						+ "IFNULL(BP.PoseeActivos,'N') as \"PoseeActivos\", "
						+ "IFNULL(X0.DESCRIPCION, '') AS Proyecto,"
						+ "IFNULL(BP.TipoRegistro,'01') AS TipoRegistro,  "
						+ "IFNULL(BP.SaldoCuenta,'') AS SaldoCuenta  "
						+ "from TB_SOCIO_NEGOCIO BP left join TB_TIPO_PERSONA TP "
						+ " ON BP.TipoPersona = TP.COD_TIP left join TB_TIPO_DOC TD "
						+ " ON BP.TipoDocumento = TD.COD_DOC left join TB_GRUPO_SOCIO_NEGOCIO G "
						+ " ON BP.GrupoSocio = G.Codigo left join TB_PROYECTO X0" +
							" ON BP.Proyecto = X0.CODIGO "
						+ "WHERE BP.Codigo ='"
						+ idBP + "'", null);

		while (rs.moveToNext()) {

			FormatCustomListView sr1 = new FormatCustomListView();
			sr1.setTitulo("Tipo de persona");
			sr1.setData(rs.getString(1));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Tipo documento");
			sr1.setData(rs.getString(2));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Nro documento");
			sr1.setData(rs.getString(3));
			searchResults.add(sr1);

			if (rs.getString(0).toString().equals("TPN")) {

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Nombre comercial");
				sr1.setData(rs.getString(4));
				searchResults.add(sr1);

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Apellido paterno");
				sr1.setData(rs.getString(5));
				searchResults.add(sr1);

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Apellido materno");
				sr1.setData(rs.getString(6));
				searchResults.add(sr1);

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Primer nombre");
				sr1.setData(rs.getString(7));
				searchResults.add(sr1);

				sr1 = new FormatCustomListView();
				sr1.setTitulo("Segundo nombre");
				sr1.setData(rs.getString(8));
				searchResults.add(sr1);

			}

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Grupo");
			sr1.setData(rs.getString(9));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Moneda");
			sr1.setData("Monedas (todas)");
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Posee Activos?");
			sr1.setData(rs.getString(rs.getColumnIndex("PoseeActivos")).equals("N") ? "NO" : "SI");
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Proyecto");
			sr1.setData(rs.getString(rs.getColumnIndex("Proyecto")));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Tipo de registro");
			sr1.setData(Constantes.obtenerTipoRegistro(rs.getString(rs.getColumnIndex("TipoRegistro"))));
			searchResults.add(sr1);

			sr1 = new FormatCustomListView();
			sr1.setTitulo("Saldo de cuenta");
			sr1.setData(rs.getString(rs.getColumnIndex("SaldoCuenta")));
			searchResults.add(sr1);
		}

		rs.close();
//		db.close();

		adapter = new ListViewCustomAdapterTwoLinesAndImg(contexto,
				searchResults);

		lvInfoBasica.setAdapter(adapter);
		DynamicHeight.setListViewHeightBasedOnChildren(lvInfoBasica);

	}

}
