## DynamicPagerAdapter

The DynamicPagerAdapter extends Android's PagerAdapter to do four important things:

* **Has an accessible HashMap View cache using ViewHolders.** The default implementaiton has caching, but it isn't enforced and users of the PagerAdapter don't get access to it.

* **Provides the capability to use multiple view types just like RecyclerView.**

* **Handles data set changes in a much more friendly way,** allowing items to be removed, added, etc. with less issues and effort on your end. 

* **Includes optional discard animations.** These are exposed for you to call when you want or to override and create your own. 


#### MyPagerAdapter.java

```java
public class MyPagerAdapter extends DynamicPagerAdapter {

    private List<Integer> values;

    public PagerAdapter(List<Integer> values) {
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup container, int position, int viewType) {
        final PagerCardView pagerCardView = new PagerCardView(container.getContext());
        return new ViewHolder(pagerCardView) {};
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        PagerCardView pagerCardView = (PagerCardView) viewHolder.view;
        pagerCardView.setViewData(values.get(position));
    }

    @Override
    public int getCount() {
        return values.size();
    }

    public void updateValues(List<Integer> values) {
        this.values = values;
        notifyDataSetChanged();
    }
}
```

#### SinglePagerActivity.java

```java
    private MyPagerAdapter pagerAdapter;
    private List<Integer> values;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_activity_view_pager);

        pagerAdapter = new MyPagerAdapter(values);
        viewPager.setAdapter(pagerAdapter);

        pagerAdapter.setCallbacks(new Callbacks() {
            @Override
            public void onDiscardFinished(int position, View view) {
                if (position != NO_POSITION) {
                    values.remove(position);
                    pagerAdapter.updateData(values);
                }
            }
        });
    }
```

## SwipeRemovalViewPager

This ViewPager subclass leverages the DynamicPagerAdapter to call fold animations on the View set after the user flings a pager View off the screen (up or down) or performs a drag-and-drop over a certain distance. It also adds helper methods for retrieving the current View from the DynamicPagerAdapter cache, among other things.

Once you have the DynamicPagerAdapter set up, using the DynamicViewPager is as easy as changing the ViewPager reference in XML to SwipeRemovalViewPager.

```xml
<com.quarkworks.dynamicviewpager.SwipeRemovalViewPager
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            ...
            />
```

## PagerContainer

This is a layout wrapper for ViewPagers that we have modified over the years. It passes touch events to the child ViewPager, allowing you to make the ViewPager whatever size you want while still accepting touches from larger areas.

# Including with Gradle

1. Add the JitPack repositorycneter to your project build.gradle:

```
allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }
}
```

2. Add the dependency to your app build.gradle:

```
dependencies {
    compile 'com.github.QuarkWorks:DynamicPagerAdapter-Android:0.9.6'
}
```
