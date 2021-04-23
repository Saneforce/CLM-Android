package saneforce.sanclm.adapter_class;

public class PresentationRecyclerAdapter /*extends DragAdapter*/ /*implements ItemMoveCallback.ItemTouchHelperContract*/ {
  /*  private LayoutInflater mInflater;
    Activity activity;
    Context mContext;
    private Filter dataFilter;
    private List<Custom_Products_GridView_Contents> origDataList;
    List<Custom_Products_GridView_Contents> mProducts_GridView_Contents;
    private OnSelectGridViewListener<String> listener;
    DataBaseHandler dbh;
    Uri imageUri;
    DetailingTrackerPOJO mDetailingTrackerPOJO;

    public PresentationRecyclerAdapter(Activity activity, int custom_presentation_search_gridview, ArrayList<Custom_Products_GridView_Contents> mProducts_gridView_contents) {
        super(activity,mProducts_gridView_contents);
        this.activity=activity;
        this.mProducts_GridView_Contents=mProducts_gridView_contents;
        this.origDataList=new ArrayList<Custom_Products_GridView_Contents>();
        this.origDataList.addAll(mProducts_GridView_Contents);
        dbh=new DataBaseHandler(activity);
        mDetailingTrackerPOJO = new DetailingTrackerPOJO();
        this.mInflater = LayoutInflater.from(this.activity);
    }

*//*
    public PresentationRecyclerAdapter(Activity activity, int mGridViewLayoutDesign, List<Custom_Products_GridView_Contents> mProducts_GridView_Contents,
                                        OnSelectGridViewListener<String> listener)
    {
        super(activity,mGridViewLayoutDesign, mProducts_GridView_Contents);
        Fresco.initialize(activity);
        this.activity=activity;
        this.mProducts_GridView_Contents=mProducts_GridView_Contents;
        this.origDataList=new ArrayList<Custom_Products_GridView_Contents>();
        this.origDataList.addAll(mProducts_GridView_Contents);
        this.listener = listener;
        dbh=new DataBaseHandler(mContext);
    }
*//*

    @Override
    public DragRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(DragRecyclerView.ViewHolder hol, final int position) {
        super.onBindViewHolder(hol, position);
        Custom_Products_GridView_Contents _product_contents = mProducts_GridView_Contents.get(position);
        final Holder holder = (Holder) hol;

*//*
        holder.iv_pdtbrand_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Custom_Products_GridView_Contents _products = mProducts_GridView_Contents.get(position);

                if (_products.mFileName == null) {
                } else {
                    if (_products.mSelectionState == true) {
                        holder.iv_pdtbrand_search.setAlpha(40);
                        _products.setmSelectionState(false);
                    } else {
                        holder.iv_pdtbrand_search.setAlpha(255);
                        _products.setmSelectionState(true);
                    }
                }
                switch (_products.getmAdapterMode())
                {
                    case CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING:
                        listener.onTaskComplete(_products.mProductBrdName,_products.mProductBrdCode);
                        break;

                    case CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS:
                        // Toast.makeText(activity, ""+_products.mFileName, Toast.LENGTH_SHORT).show();
                        //Log.d("PRODUCT_GRIDVIEW_ADAPTER_MODE", "PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS");
                        //need doctor id here
                        Log.e("products - selectionbox", _products.mSelectionState + " - " + _products.mDoctorCode + " - " + _products.mProductBrdCode + " - " + _products.mFileName + " - " + _products.mFiletype + " - " + _products.mLogoURL);
                        update_mappingTable(_products.mSelectionState,_products.mProductBrdCode,_products.mProductBrdName,_products.mFileName,_products.mFiletype,_products.mLogoURL);
                        break;
                }
            }
        });
*//*
        Bitmap bm = null ;

        if(_product_contents.mSelectionState==false){
            holder.iv_pdtbrand_search.setAlpha(40);
        }else{
            holder.iv_pdtbrand_search.setAlpha(255);
        }
        if(_product_contents.getmAdapterMode()==CommonUtils.PRODUCT_GRIDVIEW_ADAPTER_MODE_MAPPING_PRODUCTS) {
            holder.mPdt_brand.setVisibility(View.GONE);
            if(_product_contents.getmFiletype().equalsIgnoreCase("i")){
                imageUri= Uri.fromFile(new File(_product_contents.getmLogoURL()));
                Log.v("selection_img_url", String.valueOf(imageUri));
                holder.iv_pdtbrand_search.setImageURI(imageUri);
            }
            if(_product_contents.getmFiletype().equalsIgnoreCase("h"))
            {
                String HTMLPath=_product_contents.getmLogoURL().replaceAll(".zip","");
                File f = new File(HTMLPath);
                File[] files=f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(".png")||filename.contains(".jpg");
                    }
                });
                String url="";
                if (files.length>0) url=files[0].getAbsolutePath();
                imageUri= Uri.fromFile(new File(url));
                holder.iv_pdtbrand_search.setImageURI(imageUri);
            }
            if(_product_contents.getmFiletype().equalsIgnoreCase("v"))
            {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
                bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
            }
            if(_product_contents.getmFiletype().equalsIgnoreCase("p"))
            {
                Drawable myDrawable = activity.getResources().getDrawable(R.mipmap.btn_go);
                bm = ((BitmapDrawable) myDrawable).getBitmap();
                bm = scaleDown(bm, 200, true);
            }
        }else {
            if (_product_contents.getmFiletype().equalsIgnoreCase("i")) {
                imageUri = Uri.fromFile(new File(_product_contents.getmLogoURL()));
                holder.iv_pdtbrand_search.setImageURI(imageUri);
                holder.mPdt_brand.setText(_product_contents.getmProductName());
                //bm = decodeSampledBitmapFromUri(_product_contents.getmLogoURL(), 150, 150);
            }
            if (_product_contents.getmFiletype().equalsIgnoreCase("h")) {
                String HTMLPath = _product_contents.getmLogoURL().replaceAll(".zip", "");
                File f = new File(HTMLPath);
                File[] files = f.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {
                        return filename.contains(".png") || filename.contains(".jpg");
                    }
                });
                String url = "";
                if (files.length > 0) url = files[0].getAbsolutePath();
                imageUri = Uri.fromFile(new File(url));
                holder.iv_pdtbrand_search.setImageURI(imageUri);
                holder.mPdt_brand.setText(_product_contents.getmProductName());
            }
            if (_product_contents.getmFiletype().equalsIgnoreCase("v")) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(_product_contents.getmLogoURL());
                bm = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
                holder.mPdt_brand.setText(_product_contents.getmProductName());
            }


            if (_product_contents.getmFiletype().equalsIgnoreCase("p")) {
                Drawable myDrawable = activity.getResources().getDrawable(R.mipmap.btn_go);
                bm = ((BitmapDrawable) myDrawable).getBitmap();
                bm = scaleDown(bm, 200, true);
                holder.mPdt_brand.setText(_product_contents.getmProductName());
                //mViewHolder.mProductLogo.setImageBitmap(bm.createBitmap(width, height, config));
            }
        }
        if(bm!=null)
            holder.iv_pdtbrand_search.setImageBitmap(bm);

    }

    private final class Holder extends DragHolder {
        TextView mPdt_brand;
        SimpleDraweeView iv_pdtbrand_search,iv_pdtbrand_view;

        Holder(View view, int viewType) {
            super(view);
            mPdt_brand=(TextView) itemView.findViewById(R.id.tv_presentation_brd_name);
            iv_pdtbrand_search=(SimpleDraweeView) itemView.findViewById(R.id.iv_presntation_search);
            iv_pdtbrand_view=(SimpleDraweeView) itemView.findViewById(R.id.iv_presntation_view);
            iv_pdtbrand_view.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mProducts_GridView_Contents.size();
    }
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,boolean filter)
    {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),(float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }


    @SuppressLint("LongLogTag")
    protected void update_mappingTable(Boolean mSelectionState, String mProductBrdCode, String mProductBrdName, String mFileName, String mFiletype, String mLogoURL) {
        String Gpname ="1";
        try
        {
            dbh.open();

            dbh.update_presentation_selectionStatus(mDetailingTrackerPOJO.getmPrsn_svName(),mProductBrdCode,mFileName,String.valueOf(mSelectionState));
            *//*if(mDetailingTrackerPOJO.getmPrsn_svName().equals("") && mSelectionState.toString().equals("false")){


                long id = dbh.insert_into_group_mapping(Gpname ,mProductBrdCode,mProductBrdName,mFileName,mFiletype,mLogoURL,String.valueOf(mSelectionState),"1","1","1","A");
                Log.e("mapping insert listed doctor1", mFileName+" URL "+mLogoURL);
            }else if(mDetailingTrackerPOJO.getmPrsn_svName().equals("") && mSelectionState.toString().equals("true")){
                Log.e("mapping insert listed doctor12 ", mFileName);
                dbh.delete_group_mapping_table(Gpname,mProductBrdCode,mFileName,mFiletype,mLogoURL);
            }else if(mSelectionState.toString().equals("false")){
                Log.e("mapping insert listed doctor123 ",mFileName);
                dbh.delete_group_mapping_table(mDetailingTrackerPOJO.getmPrsn_svName(),mProductBrdCode,mFileName,mFiletype,mLogoURL);
            }else {
                Log.e("mapping insert listed doctor1234 ", mFileName);
                long id = dbh.insert_into_group_mapping(mDetailingTrackerPOJO.getmPrsn_svName(),mProductBrdCode,mProductBrdName,mFileName,mFiletype,mLogoURL,String.valueOf(mSelectionState),"1","1","1","A");
            }*//*
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            dbh.close();
        }
    }*/
}
