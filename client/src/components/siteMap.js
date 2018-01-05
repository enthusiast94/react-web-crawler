import React from 'react';
import ReactJson from 'react-json-view';

export default function SiteMap(props) {

    return (
        <div>
            <ReactJson src={props.sitemap} theme="harmonic" />
            <br/>
        </div>
    );
} 