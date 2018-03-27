package uk.co.jakebreen.shushevents.injection;

import uk.co.jakebreen.shushevents.view.impl.AccountMenuActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = AccountMenuViewModule.class)
public interface AccountMenuViewComponent {
    void inject(AccountMenuActivity activity);
}