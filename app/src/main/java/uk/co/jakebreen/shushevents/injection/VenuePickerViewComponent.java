package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.VenuePickerActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = VenuePickerViewModule.class)
public interface VenuePickerViewComponent {
    void inject(VenuePickerActivity activity);
}