package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.AdminRolePermission;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = AdminRolePermissionViewModule.class)
public interface AdminRolePermissionViewComponent {
    void inject(AdminRolePermission activity);
}