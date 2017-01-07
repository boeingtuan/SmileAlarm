package yolo.bachkhoa.com.smilealarm.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yolo.bachkhoa.com.smilealarm.Object.AlarmObject;
import yolo.bachkhoa.com.smilealarm.Object.FriendObject;
import yolo.bachkhoa.com.smilealarm.R;

public class FriendListFragment extends Fragment {

    private List<FriendObject> friendList = new ArrayList<FriendObject>();
    private FriendItemAdapter adapter;

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (friendList.size() == 0) {
            // Inflate the layout for this fragment
            GraphRequest graphRequest = GraphRequest.newMyFriendsRequest(
                    AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(
                                JSONArray jsonArray,
                                GraphResponse response) {
                            // Application code for users friends
                            try {
                                JSONArray friends = response.getJSONObject().getJSONArray("data");
                                for (int i = 0; i < friends.length(); i++) {
                                    JSONObject object = friends.getJSONObject(i);
                                    Log.d("friend123", object.toString());
                                    FriendObject friend = new FriendObject(object.getString("name"), object.getString("id"));
                                    friendList.add(friend);
                                }
                                Log.d("friend123", response.getJSONObject().getJSONArray("data").toString());
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            graphRequest.executeAsync();
        }

        View rootView = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ListView friend_list = (ListView) rootView.findViewById(R.id.friend_list);
        adapter = new FriendItemAdapter(friendList, this.getContext());
        friend_list.setAdapter(adapter);
        return rootView;
    }

    public class FriendItemAdapter extends BaseAdapter {

        List<FriendObject> friendList;
        Context context;

        public FriendItemAdapter(List<FriendObject> friendList, Context context) {
            this.friendList = friendList;
            this.context = context;
        }

        @Override
        public int getCount() {
            if (friendList != null)
                return friendList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return friendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row;
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();

            row = inflater.inflate(R.layout.friend_row, parent, false);

            final TextView name = (TextView) row.findViewById(R.id.name);
            //final TextView id = (TextView) row.findViewById(R.id.id);
            FriendObject object = friendList.get(position);
            name.setText(object.getName());
            //id.setText(object.getId());
            return row;
        }
    }
}
