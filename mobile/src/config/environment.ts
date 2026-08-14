import Config from 'react-native-config'; import {Platform} from 'react-native';
const localHost=Platform.OS==='android'?'10.0.2.2':'localhost';
export const API_BASE_URL=Config.API_BASE_URL?.replace(/\/$/,'')||`http://${localHost}:8080/api/v1`;
