package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.MyEventListActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MyEventListViewModule.class)
public interface MyEventListViewComponent {
    void inject(MyEventListActivity activity);
}