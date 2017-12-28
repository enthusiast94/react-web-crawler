import { createStore } from 'redux';
import { sitemapReducer } from './reducers';

export default function configureStore() {
    return createStore(sitemapReducer);
}