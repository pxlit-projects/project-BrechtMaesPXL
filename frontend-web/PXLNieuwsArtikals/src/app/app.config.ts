import {ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient} from "@angular/common/http";
import {HttpClientInMemoryWebApiModule} from "angular-in-memory-web-api";
import {InMemoryDataService} from "./shared/services/in-memory-data.service";
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {environment} from "../environments/environment";

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(),
    provideRouter(routes),
    provideAnimationsAsync(),
    // Conditionally import the in-memory web API module based on the environment
    ...(environment.useInMemoryDatabase
      ? [
        importProvidersFrom(
          HttpClientInMemoryWebApiModule.forRoot(InMemoryDataService, {
            apiBase: 'api', // Configure base API path
            dataEncapsulation: false, // Optional: Disable encapsulation for easier matching
            passThruUnknownUrl: true // Ensure other requests pass through to the real backend
          })
        )
      ]
      : [])
  ]
};
