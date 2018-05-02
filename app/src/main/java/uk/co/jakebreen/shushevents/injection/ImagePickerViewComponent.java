package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.ImagePickerActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ImagePickerViewModule.class)
public interface ImagePickerViewComponent {
    void inject(ImagePickerActivity activity);
}