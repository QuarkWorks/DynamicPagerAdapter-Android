## DynamicPagerAdapter

The DynamicPagerAdapter handles View caching and data set changes in a much more friendly way, as well as animations for dismissal of items in the ViewPager. These animations can be overriden to supply your own custom animations if you desire. DynamicPagerAdapter can be used with any ViewPager and is not dependent on the DynamicViewPager.

## DynamicViewPager

The DynamicViewPager depends on the DynamicPagerAdapter. It adds helper methods for retrieving the current View from the cache, among other things. It also listens to vertical fling and drag-and-drop gestures on the ViewPager to do animated dismissal of Views in the pager.
