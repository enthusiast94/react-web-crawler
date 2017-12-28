import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'bootstrap/dist/css/bootstrap.css';
import App from './containers/App';
import Provider from './containers/Provider';
import configureStore from './redux/configureStore';

const store = configureStore();
ReactDOM.render(<Provider store={store}><App /></Provider>, document.getElementById('root'));
