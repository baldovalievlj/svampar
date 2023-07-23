import { ConfigurationType } from "./configuration-type";

export interface Configuration{
  key: string,
  value: string,
  type: ConfigurationType
}
