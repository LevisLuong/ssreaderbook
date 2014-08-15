/*
Copyright 2014 David Morrissey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package vn.seasoft.readerbook.widget;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import org.holoeverywhere.app.Fragment;
import vn.seasoft.readerbook.R;

public class ViewPagerFragment extends Fragment {
    private Bitmap bitmap;

    public ViewPagerFragment() {
    }

    public ViewPagerFragment(Bitmap _bitmap) {
        this.bitmap = _bitmap;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_pager_page, container, false);
        SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) rootView.findViewById(R.id.imageView);
        return rootView;
    }

}
